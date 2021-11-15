package com.devteam.core.data.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
