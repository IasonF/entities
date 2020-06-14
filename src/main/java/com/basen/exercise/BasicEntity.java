package com.basen.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicEntity implements Entity {
  private String id;
  private Set<String> subEntities;
  private Map<String, String> data;
}
