package com.shawn.geektime.homework.user.db.exception;

/**
 * the SQL operations are not supported
 *
 * @author Shawn
 * @since 1.0
 */
public class NoSuchSqlOperationsException extends JdbcTemplateException {

  public NoSuchSqlOperationsException(String message) {
    super(message);
  }

  public NoSuchSqlOperationsException(Throwable cause) {
    super(cause);
  }

  public NoSuchSqlOperationsException(String message, Throwable cause) {
    super(message, cause);
  }
}
