package org.spell.exception;

import lombok.Getter;

@Getter
public class OptionException extends BaseShellException {

  public OptionException(String message) {
    super(message, 2);
  }
}
