package com.devteam.core.entity;

import com.devteam.core.enums.StorageState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @Getter @Setter
public class ChangeStorageStateRequest {
  @Enumerated(EnumType.STRING)
  private StorageState newStorageState;
  private List<String>  entityIds;

  public ChangeStorageStateRequest(StorageState newState) {
    this.newStorageState = newState;
  }

  public ChangeStorageStateRequest withEntityId(String id) {
    if(entityIds == null) entityIds = new ArrayList<>();
    entityIds.add(id);
    return this;
  }
}
