package com.basen.exercise;

import java.util.Map;
import java.util.Set;

public interface Entity {
  // Returns a unique identifier
  String getId();

  // Returns the ID's of the sub-entities of this entity
  Set<String> getSubEntities();

  // Returns a set of key-value data belonging to this entity
  Map<String, String> getData();
}
