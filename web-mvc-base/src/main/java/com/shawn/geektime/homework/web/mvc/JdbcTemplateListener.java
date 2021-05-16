package com.shawn.geektime.homework.web.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JdbcTemplateListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
