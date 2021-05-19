package com.shawn.geektime.homework.user.common.exception;

/**
 * jndi component inject failed exception
 *
 * @author Shawn
 * @since 1.0
 */
public class InjectionComponentFailedException extends RuntimeException {

  public InjectionComponentFailedException(String message) {
    super(message);
  }

  public InjectionComponentFailedException(Throwable cause) {
    super(cause);
  }

  public InjectionComponentFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
