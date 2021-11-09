package com.devteam.data.repository;

import com.devteam.config.ClientInfo;
import com.devteam.data.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface TenantRepository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {
  T getOne(ClientInfo client, Object[][] keyValues);
  T getOne(ClientInfo client, String filter, Object[][] params);

  List<T> getAll(ClientInfo client);

  Page<T> findAll(ClientInfo client, Pageable pageable);

  List<T> find(ClientInfo client, Object[][] keyValues);
  List<T> find(ClientInfo client, String filter, Object[][] params);

  <R> List<R> query(ClientInfo client, String jpql, Object[][] params);

  <R> List<R> customTenantQuery(ClientInfo client, String jpql, Object[][] params);

  T save(ClientInfo client, T entity);
  List<T> saveAll(ClientInfo client, List<T> entity);

  T save(ClientInfo client, T entity, boolean checkModified);
  List<T> saveAll(ClientInfo client, List<T> entity, boolean checkModified);

  int delete(ClientInfo client, Object[][] keyValues);
  int delete(ClientInfo client, String filter, Object[][] params);

  void delete(ClientInfo client, T entity);
  void deleteAll(ClientInfo client);
  void deleteAll(ClientInfo client, List<T> entity);

  List<Long> getAvailableIds(ClientInfo client);

  long count(ClientInfo client, Object[][] keyValues);
  long count(ClientInfo client, String filter, Object[][] params);
}