package com.devteam.data.activity.entity;

import com.devteam.data.entity.BaseEntity;
import com.devteam.util.ds.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = TransactionActivity.TABLE_NAME, indexes = { @Index(columnList = "name") })
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class TransactionActivity extends BaseEntity<String> {
  private static final long serialVersionUID = 1L;

  public static final String TABLE_NAME = "core_activity_transaction";

  private String name;
  private String label;
  @Column(length = 64 * 1024)
  private String description;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "transaction_activity_id", referencedColumnName = "id")
  private List<EntityActivity> entityActivities;

  public TransactionActivity withActivity(EntityActivity... activities) {
    entityActivities = Arrays.addToList(entityActivities, activities);
    return this;
  }

  public TransactionActivity withGeneratedDescription() {
    StringBuilder b = new StringBuilder();
    for (EntityActivity entity : entityActivities) {
      b.append(entity.generateDescription());
      b.append("\n");
    }
    description = b.toString();
    if (description.length() > (64 * 1024)) {
      System.out.println(description);
      description = description.substring(0, 64 * 1024);
    }
    return this;
  }

  public void resolveInfo() {
    if (entityActivities != null) {
      for (EntityActivity entity : entityActivities) {
        entity.resolveInfo();
      }
    }
  }
}
