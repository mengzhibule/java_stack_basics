package com.shawn.geektime.homework.user.db.exception;

public class JdbcTemplateException extends RuntimeException {

  public JdbcTemplateException(String message) {
    super(message);
  }

  public JdbcTemplateException(Throwable cause) {
    super(cause);
  }

  public JdbcTemplateException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
