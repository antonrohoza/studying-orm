package com.antonr.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.antonr.orm.entity.Person;
import com.antonr.orm.entity.TestPersonWithoutId;
import com.antonr.orm.entity.TestPersonWithoutTable;
import com.antonr.orm.exception.NoSuchIdException;
import org.junit.jupiter.api.Test;

public class QueryGeneratorTest {

  QueryGenerator queryGenerator = new DefaultQueryGenerator();

  @Test
  public void findAllTest() {
    String expectedQuery = "SELECT person_id,name,age FROM Person";
    String actualQuery = queryGenerator.findAll(Person.class);

    assertEquals(expectedQuery, actualQuery);
  }

  @Test
  public void findById() {
    String expectedQuery = "SELECT person_id,name,age FROM Person WHERE person_id=1";
    String actualQuery = queryGenerator.findById(1, Person.class);

    assertEquals(expectedQuery, actualQuery);
  }

  @Test
  public void insert() {
    String expectedQuery = "INSERT INTO Person (person_id,name,age) VALUES ('1','A','25')";
    String actualQuery = queryGenerator.insert(new Person(1, "A", 25));

    assertEquals(expectedQuery, actualQuery);
  }

  @Test
  public void delete() {
    String expectedQuery = "DELETE FROM Person WHERE person_id=1";
    String actualQuery = queryGenerator.delete(new Person(1, "A", 25));

    assertEquals(expectedQuery, actualQuery);
  }

  @Test
  public void deletingWithoutIdAnnotationShouldThrowNoSuchIdException() {

    assertThrows(NoSuchIdException.class,
        () -> queryGenerator.delete(new TestPersonWithoutId(1, "A", 24)));
  }

  @Test
  public void findByIdWithoutIdAnnotationShouldThrowNoSuchIdException() {

    assertThrows(NoSuchIdException.class,
        () -> queryGenerator.findById(1, TestPersonWithoutId.class));
  }

  @Test
  public void deletingWithoutTableAnnotationShouldThrowIllegalArgumentException() {

    assertThrows(IllegalArgumentException.class,
        () -> queryGenerator.delete(new TestPersonWithoutTable(2, "B", 24)));
  }
}
