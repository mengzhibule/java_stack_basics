package com.shawn.geektime.homework.web.mvc;

import com.shawn.geektime.homework.web.mvc.annotation.RequestMapping;
import com.shawn.geektime.homework.web.mvc.api.Controller;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

  /**
   * 存储所有实现了Controller interface的类，或者标记了@Controller的类
   */
  private final Map<String, Object> controllerMappings = new ConcurrentHashMap<>();

  private final Map<String, Method> handlerMethodMappings = new ConcurrentHashMap<>();

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doDispatcher(request, response);
  }

  @Override
  public void init() throws ServletException {
    // 扫包
    String scanPackage = "com.shawn.geektime.homework";
    doScanner(scanPackage);
    // 处理requestMapping
    initHandlerMethod();
  }

  private void doDispatcher(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String url = request.getRequestURI();
    String contextPath = request.getContextPath();
    url = url.replace(contextPath, "").replaceAll("/+", "/").replaceAll("//", "/");
    Method method = handlerMethodMappings.get(url);
    if (Objects.isNull(method)) {
      response.getWriter().write("404 NOT FOUND!");
      return;
    }
    Class<?>[] parameterTypes = method.getParameterTypes();
    Map<String, String[]> parameterMap = request.getParameterMap();
    Object[] params = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      String requestParam = parameterTypes[i].getSimpleName();
      if (requestParam.equals("HttpServletRequest")) {
        params[i] = request;
        continue;
      }
      if (requestParam.equals("HttpServletResponse")) {
        params[i] = response;
        continue;
      }
      if (requestParam.equals("String")) {
        for (Entry<String, String[]> param : parameterMap.entrySet()) {
          String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "")
              .replaceAll(",\\s", ",");
          params[i] = value;
        }
      }
    }
    //利用反射机制来调用
    try {
      method.invoke(this.controllerMappings.get(url), params);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  private void doScanner(String scanPackage) {
    URL resource = this.getClass().getClassLoader()
        .getResource("/" + scanPackage.replaceAll("\\.", "/"));
    if (Objects.isNull(resource)) {
      return;
    }
    File classPath = new File(resource.getFile());
    for (File file : Objects.requireNonNull(classPath.listFiles())) {
      if (file.isDirectory()) {
        doScanner(scanPackage + "." + file.getName());
      } else {
        if (!file.getName().endsWith(".class")) {
          continue;
        }
        String className = scanPackage + "." + file.getName().replace(".class", "");
        createInstance(className);
      }
    }
  }

  private void createInstance(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      if (clazz.isAnnotationPresent(com.shawn.geektime.homework.web.mvc.annotation.Controller.class)
          || Arrays.asList(clazz.getInterfaces()).contains(Controller.class)) {
        Object instance = clazz.newInstance();
        String beanName = clazz.getName();
        controllerMappings.put(beanName, instance);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initHandlerMethod() {
    Set<String> beanNames = controllerMappings.keySet();
    for (String beanName : beanNames) {
      Class<?> controller = controllerMappings.get(beanName).getClass();
      if (!(controller
          .isAnnotationPresent(com.shawn.geektime.homework.web.mvc.annotation.Controller.class)
          || Arrays.asList(controller.getInterfaces()).contains(Controller.class))) {
        continue;
      }
      String url = "";
      String rootUrl = "";
      if (controller.isAnnotationPresent(RequestMapping.class)) {
        RequestMapping requestMapping = controller.getAnnotation(RequestMapping.class);
        rootUrl = requestMapping.value();
      }

      for (Method method : controller.getMethods()) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
          url = "/" + rootUrl + "/" + requestMapping.value();
          handlerMethodMappings.put(url, method);
        }
      }
    }
  }
}
