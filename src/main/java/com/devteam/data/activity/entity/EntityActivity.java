package com.devteam.data.activity.entity;

import com.devteam.data.entity.BaseEntity;
import com.devteam.data.entity.Persistable;
import com.devteam.util.bean.ClassUtil;
import com.devteam.util.dataformat.DataSerializer;
import com.devteam.util.text.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
  name = EntityActivity.TABLE_NAME,
  indexes = { @Index(columnList = "name") }
)
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor @Getter @Setter
public class EntityActivity extends BaseEntity<String> {
  private static final long  serialVersionUID = 1L;
  public static final String TABLE_NAME = "core_activity_entity";

  private String       name;
  @Column(name="entity_table")
  private String       entityTable;
  @Column(name="entity_id")
  private Long         entityId;
  @Column(name="entity_label")
  private String       entityLabel;
  private ChangeAction action = null;

  @Column(name = "transaction_activity_id", insertable=false, updatable=false)
  private Long transactionActivityId;

  @JsonIgnore
  @Transient
  private Object entity;

  @Transient
  private List<EntityFieldChange> fieldChanges;

  private EntityActivity(Object entity, Long entityId) {
    this.name = entity.getClass().getSimpleName();
    this.entityId = entityId;
    Table tableAnn = entity.getClass().getAnnotation(Table.class) ;
    if(tableAnn != null) entityTable = tableAnn.name();
    this.entity = entity;
  }

  public EntityActivity(Object entity, Long entityId, Object[] state, String[] fieldNames, Type[] types) {
    this(entity, entityId);
    this.action = ChangeAction.New;
    fieldChanges = new ArrayList<>();
    for(int i = 0; i < state.length; i++) {
      if(state[i] == null) continue;
      if(!ClassUtil.isSqlType(state[i].getClass())) continue;
      EntityFieldChange field = new EntityFieldChange(this.action, fieldNames[i], null, state[i]);
      fieldChanges.add(field);
    }
  }

  public EntityActivity(
      Object entity, Long entityId, Object[] currentState, Object[] previousState, String[] fieldNames, Type[] types) {
    this(entity, entityId);
    this.action = ChangeAction.Modified;
    fieldChanges = new ArrayList<>();
    for(int i = 0; i < currentState.length; i++) {
      Object pState = previousState[i];
      Object cState = currentState[i];
      if(cState == null && pState == null) continue;
      if(cState != null) {
        if(!ClassUtil.isSqlType(cState.getClass())) continue;
        if(cState.equals(pState)) continue;
      }
      EntityFieldChange field = new EntityFieldChange(this.action, fieldNames[i], previousState[i], currentState[i]);
      fieldChanges.add(field);
    }
  }


  @JsonIgnore
  @Access(AccessType.PROPERTY)
  @Column(name="field_changes_json", length =  64 * 1024)
  public String getFieldChangesJson() {
    if(this.fieldChanges == null) return null;
    String json = DataSerializer.JSON.toString(this.fieldChanges);
    return json;
  }

  public void setFieldChangesJson(String json) {
    if(StringUtil.isEmpty(json)) {
      this.fieldChanges = null;
    } else {
      this.fieldChanges = DataSerializer.JSON.fromString(json, new TypeReference<List<EntityFieldChange>>() {});
    }
  }

  public String generateDescription() {
    StringBuilder b = new StringBuilder();
    b.append(name).append(": \n");
    for(int i = 0; i < fieldChanges.size(); i++) {
      EntityFieldChange field = fieldChanges.get(i);
      b.append("  ");
      b.append(field.getFieldName()).append(": ").append(field.getOldValue()).append(" -> ").append(field.getNewValue());
      b.append("\n");
    }
    return b.toString();
  }

  public void resolveInfo() {
    if(entity != null && entity instanceof Persistable) {
      Persistable<Long> persistable = (Persistable<Long>) entity;
      if(ChangeAction.New.equals(action)) {
        entityId = persistable.getId();
      }
      entityLabel = persistable.label();
    }
    entity = null;
  }
}