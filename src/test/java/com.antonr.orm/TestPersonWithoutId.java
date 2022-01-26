package com.antonr.orm;

import com.antonr.orm.annotation.Column;
import com.antonr.orm.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table
public class TestPersonWithoutId {

  @Column(name = "person_id")
  private int id;

  @Column
  private String name;

  @Column
  private int age;

}
