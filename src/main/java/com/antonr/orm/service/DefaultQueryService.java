package com.antonr.orm.service;

import static com.antonr.orm.Constants.DELIMITER;
import static com.antonr.orm.Constants.NOT_ORM_TABLE_EXCEPTION;
import static com.antonr.orm.Constants.NO_FIELD_VALUE_EXCEPTION;

import com.antonr.orm.annotation.Column;
import com.antonr.orm.annotation.Id;
import com.antonr.orm.annotation.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultQueryService {

  public static Optional<Field> getIdFieldInTable(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Id.class))
        .findFirst();
  }

  public static String getFieldValue(Field field, Object value) {
    try {
      field.setAccessible(true);
      return String.valueOf(field.get(value));
    } catch (Exception ex) {
      throw new RuntimeException(NO_FIELD_VALUE_EXCEPTION + field.getName());
    }
  }

  public static String getParametersString(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Column.class))
        .map(DefaultQueryService::getColumnName)
        .collect(Collectors.joining(DELIMITER));
  }

  public static String getColumnName(Field field) {
    String columnAnnotationName = field.getDeclaredAnnotation(Column.class).name();
    return !columnAnnotationName.isEmpty() ? columnAnnotationName : field.getName();
  }

  public static String getTableName(Class<?> clazz) {
    final Table tableAnnotation = getTableAnnotation(clazz);
    String annotationName = tableAnnotation.name();
    return !annotationName.isEmpty() ? annotationName : clazz.getSimpleName();
  }

  private static Table getTableAnnotation(Class<?> clazz) {
    if (!clazz.isAnnotationPresent(Table.class)) {
      throw new IllegalArgumentException(clazz.getName() + NOT_ORM_TABLE_EXCEPTION);
    }
    return clazz.getAnnotation(Table.class);
  }

}
