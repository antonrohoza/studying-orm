package com.antonr.orm;

public enum Constants {
  ;
  public static final String DELIMITER = ",";
  static final String SELECT = "SELECT ";
  static final String FROM = " FROM ";
  static final String OPEN_BRACKET = " (";
  static final String CLOSE_BRACKET = ")";
  static final String WHERE = " WHERE ";
  static final String INSERT = "INSERT";
  static final String INTO = " INTO ";
  static final String VALUES = " VALUES";
  static final String DELETE = "DELETE";
  static final String IS_EQUAL = "=";

  public static final String NOT_ORM_TABLE_EXCEPTION = " isn't an ORM table!";
  public static final String NO_FIELD_VALUE_EXCEPTION = "No value in the field: ";
  static final String NO_ID_IN_TABLE_EXCEPTION = "There is no id in table ";
}
