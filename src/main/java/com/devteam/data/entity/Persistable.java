package com.devteam.data.entity;

import com.devteam.config.ClientInfo;
import com.devteam.data.enums.EditState;
import com.devteam.data.enums.LoadState;
import com.devteam.util.ds.IdentifiableObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter @Setter
abstract  public class Persistable<PK extends Serializable>
  implements org.springframework.data.domain.Persistable<PK>, IdentifiableObject, Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  protected PK     id;

  @Transient
  protected EditState editState = EditState.ORIGIN;

  @Transient
  protected LoadState loadState = LoadState.FromDB;

  public void markDeleted() { markState(EditState.DELETED); }

  public void markModified() { markState(EditState.MODIFIED); }

  protected void markState(EditState state) {
    if(isNew()) {
      throw new RuntimeException("Cannot mark the state when the object is new");
    }
    this.editState = state;
  }

  @JsonIgnore
  @Transient
  public boolean isNew() { return null == getId(); }

  public void set(ClientInfo client) {
  }

  public void set(ClientInfo client, Date time) {
  }

  public void set(String remoteUser, Date time) {
  }

  public int hashCode() {
    int hashCode = 17;
    hashCode += null == getId() ? 0 : getId().hashCode() * 31;
    return hashCode;
  }

  public String label() { return "N/A"; }

  public String identify() {
    if(id == null) return null;
    return id.toString();
  }
}