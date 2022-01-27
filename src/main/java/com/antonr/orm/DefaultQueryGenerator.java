package com.antonr.orm;

import static com.antonr.orm.Constants.*;
import static com.antonr.orm.service.DefaultQueryService.getFieldValue;
import static com.antonr.orm.service.DefaultQueryService.getIdFieldInTable;
import static com.antonr.orm.service.DefaultQueryService.getParametersString;
import static com.antonr.orm.service.DefaultQueryService.getTableName;

import com.antonr.orm.exception.NoSuchIdException;
import com.antonr.orm.service.DefaultQueryService;
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
    Optional<String> idColumnName = idField.map(DefaultQueryService::getColumnName);

    if (idColumnName.isEmpty()) {
      throw new NoSuchIdException(NO_ID_IN_TABLE_EXCEPTION + tableName);
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
    Optional<String> idValue = idField.map(field -> getFieldValue(field, value));
    Optional<String> idColumnName = idField.map(DefaultQueryService::getColumnName);

    if (idValue.isEmpty() || idColumnName.isEmpty()) {
      throw new NoSuchIdException(NO_ID_IN_TABLE_EXCEPTION + tableName);
    } else {
      return DELETE
          + FROM
          + tableName
          + WHERE
          + idColumnName.get() + IS_EQUAL + idValue.get();
    }
  }
}
