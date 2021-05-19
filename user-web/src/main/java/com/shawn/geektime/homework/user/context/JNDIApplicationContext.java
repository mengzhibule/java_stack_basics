package com.shawn.geektime.homework.user.context;

import com.shawn.geektime.homework.user.common.exception.InjectionComponentFailedException;
import com.shawn.geektime.homework.user.common.function.ThrowableAction;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import org.apache.commons.collections.CollectionUtils;

/**
 * Implement the application context based on JNDI
 *
 * @author Shawn
 * @since 1.0
 */
public class JNDIApplicationContext implements ApplicationContext {

  private static final String CONTEXT_NAME = JNDIApplicationContext.class.getName();

  private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

  private static final Logger LOGGER = Logger.getLogger(CONTEXT_NAME);

  private static ServletContext servletContext;

  private ClassLoader classLoader;

  private Context envContext;

  private static final Map<String, Object> COMPONENT_MAP = new HashMap<>();

  private static final Map<Method, Object> CANDIDATE_DESTROY_MAP = new HashMap<>();

  private static final List<String> COMPONENT_NAMES = new ArrayList<>();

  public static JNDIApplicationContext getInstance() {
    return (JNDIApplicationContext) servletContext.getAttribute(CONTEXT_NAME);
  }

  public void init(ServletContext servletContext) throws RuntimeException {
    JNDIApplicationContext.servletContext = servletContext;
    servletContext.setAttribute(CONTEXT_NAME, this);
    this.init();
  }

  @Override
  public void init() {
    initClassLoader();
    initEnvContext();
    initComponents();
    initInjection();
    registerShutdownHook();
  }

  @Override
  public void destroy() {}

  @Override
  @SuppressWarnings("unchecked")
  public <C> C getComponent(String componentName) {
    return (C) COMPONENT_MAP.get(componentName);
  }

  @Override
  public List<String> getComponentNames() {
    return null;
  }

  private void initEnvContext() {
    if (this.envContext != null) {
      return;
    }
    Context context = null;
    try {
      context = new InitialContext();
      this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
    } catch (NamingException e) {
      throw new RuntimeException(e);
    } finally {
      close(context);
    }
  }

  private void initClassLoader() {
    this.classLoader = servletContext.getClassLoader();
  }

  private void initComponents() {
    listComponentNames("/");
    COMPONENT_NAMES.forEach(name -> COMPONENT_MAP.put(name, lookUpComponent(name)));
  }

  private void listComponentNames(String name) {
    try {
      NamingEnumeration<NameClassPair> elements = this.envContext.list(name);
      if (elements == null) {
        return;
      }
      while (elements.hasMoreElements()) {
        NameClassPair element = elements.nextElement();
        String className = element.getClassName();
        Class<?> targetClass = this.classLoader.loadClass(className);
        if (Context.class.isAssignableFrom(targetClass)) {
          // 如果当前名称是目录（Context 实现类）的话，递归查找
          listComponentNames(element.getName());
        } else {
          // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
          String fullName =
              name.startsWith("/") ? element.getName() : name + "/" + element.getName();
          COMPONENT_NAMES.add(fullName);
        }
      }
    } catch (NamingException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private <C> C lookUpComponent(String name) {
    try {
      return (C) envContext.lookup(name);
    } catch (NamingException e) {
      throw new RuntimeException(e);
    }
  }

  private void initInjection() {
    COMPONENT_MAP.values().forEach(this::injectComponent);
  }

  private void injectComponent(Object component) {
    if (Objects.isNull(component)) {
      return;
    }
    Class<?> componentClass = component.getClass();
    injectComponent(component, componentClass);
    List<Method> candidateMethods = findCandidateMethods(componentClass);
    processPostConstruct(component, candidateMethods);
    processPreDestroyMetadata(component, candidateMethods);
  }

  private void injectComponent(Object component, Class<?> type) {
    if (Objects.isNull(component)) {
      return;
    }
    Stream.of(type.getDeclaredFields())
        .filter(
            field ->
                !Modifier.isStatic(field.getModifiers())
                    && field.isAnnotationPresent(Resource.class))
        .forEach(
            field -> {
              Resource resource = field.getAnnotation(Resource.class);
              String name = resource.name();
              Object injectObject = lookUpComponent(name);
              field.setAccessible(true);
              try {
                field.set(component, injectObject);
              } catch (IllegalAccessException e) {
                throw new InjectionComponentFailedException(
                    String.format(
                        "The dependency injection component failed, type: [%s], component: [%s]",
                        type.getName(), component.getClass().getName()),
                    e);
              }
            });
  }

  private List<Method> findCandidateMethods(Class<?> type) {
    if (Objects.isNull(type)) {
      return null;
    }
    return Stream.of(type.getMethods())
        .filter(
            method ->
                !Modifier.isStatic(method.getModifiers()) && method.getParameters().length == 0)
        .collect(Collectors.toList());
  }

  private void processPostConstruct(Object component, List<Method> methods) {
    if (Objects.isNull(component) || CollectionUtils.isEmpty(methods)) {
      return;
    }
    methods
        .stream()
        .filter(method -> method.isAnnotationPresent(PostConstruct.class))
        .forEach(method -> ThrowableAction.execute(() -> method.invoke(component)));
  }

  private void processPreDestroyMetadata(Object component, List<Method> methods) {
    if (Objects.isNull(component) || CollectionUtils.isEmpty(methods)) {
      return;
    }
    methods
        .stream()
        .filter(method -> method.isAnnotationPresent(PreDestroy.class))
        .forEach(method -> CANDIDATE_DESTROY_MAP.put(method, component));
  }

  private void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(this::processPreDestroy));
  }

  private void processPreDestroy() {
    for (Method method : CANDIDATE_DESTROY_MAP.keySet()) {
      // 移除集合中的对象，防止重复执行 @PreDestroy 方法
      Object component = CANDIDATE_DESTROY_MAP.remove(method);
      // 执行目标方法
      ThrowableAction.execute(() -> method.invoke(component));
    }
  }

  private static void close(Context context) {
    if (context != null) {
      ThrowableAction.execute(context::close);
    }
  }
}
