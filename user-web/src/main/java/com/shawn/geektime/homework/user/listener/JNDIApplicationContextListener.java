package com.shawn.geektime.homework.user.listener;

import com.shawn.geektime.homework.user.context.ApplicationContext;
import com.shawn.geektime.homework.user.context.JNDIApplicationContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * start jndi application context
 *
 * @author shawn
 * @since 1.0
 */
public class JNDIApplicationContextListener implements ServletContextListener {

  private ServletContext servletContext;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    this.servletContext = sce.getServletContext();
    JNDIApplicationContext context = new JNDIApplicationContext();
    context.init(servletContext);
    DataSource dataSource = context.getComponent("jdbc/UserPlatformDB");
    String simpleName = dataSource.getClass().getSimpleName();
    System.out.println(simpleName);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ApplicationContext context = JNDIApplicationContext.getInstance();
    context.destroy();
  }
}
