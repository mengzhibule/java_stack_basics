package com.shawn.geektime.homework.user.common.exception;

/**
 * function execute exception
 *
 * @author Shawn
 * @since 1.0
 */
public class CommonExecuteException extends RuntimeException {

  public CommonExecuteException(String message) {
    super(message);
  }

  public CommonExecuteException(Throwable cause) {
    super(cause);
  }

  public CommonExecuteException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
