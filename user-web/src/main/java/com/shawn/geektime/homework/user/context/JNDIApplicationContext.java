package com.shawn.geektime.homework.user.context;

import com.shawn.geektime.home.user.common.function.ThrowableAction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

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

  private static Map<String, Object> componentMap = new HashMap<>();

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
  }

  @Override
  public void destroy() {}

  @Override
  public <C> C getComponent(String componentName) {
    try {
      DataSource ds = (DataSource) this.envContext.lookup("jdbc/UserPlatformDB");
      Connection connection = ds.getConnection();
      LOGGER.info("connect succeed");
    } catch (NamingException | SQLException e) {
      e.printStackTrace();
    }
    return null;
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

  private static void close(Context context) {
    if (context != null) {
      ThrowableAction.execute(context::close);
    }
  }
}
