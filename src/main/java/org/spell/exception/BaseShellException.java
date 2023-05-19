package org.spell.exception;

import lombok.Getter;
import org.springframework.boot.ExitCodeGenerator;

public abstract class BaseShellException extends RuntimeException implements ExitCodeGenerator {

  private final int exitCode;

  public BaseShellException(String message, int exitCode) {
    super(message);
    this.exitCode = exitCode;
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }

}
