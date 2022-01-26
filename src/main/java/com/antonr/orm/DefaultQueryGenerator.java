package com.antonr.orm;

import static com.antonr.orm.Constants.*;

import com.antonr.orm.annotation.Column;
import com.antonr.orm.annotation.Id;
import com.antonr.orm.annotation.Table;
import com.antonr.orm.exception.NoSuchIdException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultQueryGenerator implements QueryGenerator {

  @Override
  public String findAll(Class<?> clazz) {

    return SELECT
        + getParametersString(clazz)
        + FROM
        + getTableName(clazz);
  }

  @Override
  public String findById(Serializable id, Class<?> clazz) {
    String tableName = getTableName(clazz);
    Optional<Field> idField = getIdFieldInTable(clazz);
    Optional<String> idColumnName = idField.map(this::getColumnName);

    if (idColumnName.isEmpty()) {
      throw new NoSuchIdException("There is no id in table " + tableName);
    } else {
      return SELECT
          + getParametersString(clazz)
          + FROM
          + tableName
          + WHERE
          + idColumnName.get() + IS_EQUAL + id;
    }
  }

  @Override
  public String insert(Object value) {
    Class<?> clazz = value.getClass();
    String values = Arrays.stream(clazz.getDeclaredFields())
        .map(field -> getFieldValue(field, value))
        .map(fieldValue -> "'" + fieldValue + "'")
        .collect(Collectors.joining(DELIMITER));

    return INSERT
        + INTO
        + getTableName(clazz)
        + OPEN_BRACKET + getParametersString(clazz) + CLOSE_BRACKET
        + VALUES
        + OPEN_BRACKET + values + CLOSE_BRACKET;
  }

  @Override
  public String delete(Object value) {
    Class<?> clazz = value.getClass();
    String tableName = getTableName(clazz);
    Optional<Field> idField = getIdFieldInTable(clazz);
    Optional<String> id = idField.map(field -> getFieldValue(field, value));
    Optional<String> idColumnName = idField.map(field -> getColumnName(idField.get()));

    if (id.isEmpty() || idColumnName.isEmpty()) {
      throw new NoSuchIdException("There is no id in table " + tableName);
    } else {
      return DELETE
          + FROM
          + tableName
          + WHERE
          + idColumnName.get() + IS_EQUAL + id.get();
    }
  }

  private Optional<Field> getIdFieldInTable(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Id.class))
        .findFirst();
  }

  private String getFieldValue(Field field, Object value) {
    String fieldValue = "";
    try {
      field.setAccessible(true);
      fieldValue = String.valueOf(field.get(value));
    } catch (Exception ex) {
      throw new RuntimeException("No value in the field +" + field.getName());
    }
    return fieldValue;
  }

  private String getTableName(Class<?> clazz) {
    final Table tableAnnotation = getTableAnnotation(clazz);
    String annotationName = tableAnnotation.name();
    return !annotationName.isEmpty() ? annotationName : clazz.getSimpleName();
  }

  private Table getTableAnnotation(Class<?> clazz) {
    if (!clazz.isAnnotationPresent(Table.class)) {
      throw new IllegalArgumentException(clazz.getName() + " isn't a ORM table!");
    }
    return clazz.getAnnotation(Table.class);
  }

  private String getParametersString(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Column.class))
        .map(this::getColumnName)
        .collect(Collectors.joining(DELIMITER));
  }

  private String getColumnName(Field field) {
    String columnAnnotationName = field.getDeclaredAnnotation(Column.class).name();
    return !columnAnnotationName.isEmpty() ? columnAnnotationName : field.getName();
  }
}
