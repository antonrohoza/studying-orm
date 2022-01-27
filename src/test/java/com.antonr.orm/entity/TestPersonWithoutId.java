package com.antonr.orm.entity;

import com.antonr.orm.annotation.Column;
import com.antonr.orm.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
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
