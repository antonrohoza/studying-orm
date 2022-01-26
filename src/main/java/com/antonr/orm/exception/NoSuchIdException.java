package com.antonr.orm.exception;

public class NoSuchIdException extends RuntimeException {

  public NoSuchIdException(String msg) {
    super(msg);
  }
}
