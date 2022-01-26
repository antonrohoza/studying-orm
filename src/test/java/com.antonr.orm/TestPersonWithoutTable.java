package com.antonr.orm;

import com.antonr.orm.annotation.Column;
import com.antonr.orm.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TestPersonWithoutTable {

  @Id
  @Column(name = "person_id")
  private int id;

  @Column
  private String name;

  @Column
  private int age;

}
