package com.devteam.data.activity;

import com.devteam.config.ClientInfo;
import com.devteam.config.LogTransactionActivity;
import com.devteam.data.DAOService;
import com.devteam.data.DataThreadContext;
import com.devteam.data.SqlMapRecord;
import com.devteam.data.activity.entity.EntityActivity;
import com.devteam.data.activity.entity.TransactionActivity;
import com.devteam.data.activity.repository.EntityActivityRepository;
import com.devteam.data.activity.repository.TransactionActivityRepository;
import com.devteam.data.query.*;
import com.devteam.util.ds.Collections;
import com.devteam.util.text.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Aspect
@Component
@Order(10)
public class TransactionActivityService extends DAOService {
  @Autowired
  private DataThreadContext threadContext ;

  @Autowired
  private TransactionActivitySource channelSource;

  @Autowired
  private TransactionActivityRepository repo;

  @Autowired
  private EntityActivityRepository entityActivityRepo;


  @Transactional(readOnly = true)
  public TransactionActivity getTransactionActivity(ClientInfo client, Long id) {
    TransactionActivity tActivity = repo.getById(id);
    return tActivity;
  }

  @Transactional
  public TransactionActivity saveTransactionActivity(ClientInfo client, TransactionActivity tActivity) {
    tActivity.set(client);
    tActivity.withGeneratedDescription();
    return repo.save(tActivity);
  }

  @Transactional(readOnly = true)
  public List<SqlMapRecord> searchTransactionActivities(ClientInfo client, SqlQueryParams params) {
    String[] SEARCH_FIELDS = { "name" };
    SqlQuery query = new SqlQuery()
        .ADD_TABLE(new EntityTable(TransactionActivity.class).selectAllFields())
        .FILTER(new SearchFilter(TransactionActivity.class, SEARCH_FIELDS, "LIKE", "search"))
        .FILTER(new ParamFilter(TransactionActivity.class, "name", "=", "name"))
        .FILTER(RangeFilter.modifiedTime(TransactionActivity.class))
        .ORDERBY(new String[] {"modifiedTime"}, "modifiedTime", "ASC");
    return query(client, query, params).getSqlMapRecords();
  }

  @Transactional(readOnly = true)
  public List<EntityActivity> findEntityActivityByEntityId(ClientInfo client, String entityTable, Long entityId) {
    return entityActivityRepo.findByEntityId(entityTable, entityId);
  }

  @Around("@annotation(com.devteam.config.LogTransactionActivity)")
  public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature)joinPoint.getSignature();
    LogTransactionActivity annotation = signature.getMethod().getAnnotation(LogTransactionActivity.class);
    TransactionActivity tActivity = new TransactionActivity();
    String name = annotation.name();
    if(StringUtil.isEmpty(name)) name = annotation.value();
    tActivity.setName(name);
    tActivity.setLabel(annotation.label());
    threadContext.setCurrentTransactionActivity(tActivity);

    Object result = joinPoint.proceed();
    if(!Collections.isEmpty(tActivity.getEntityActivities())) {
      tActivity.resolveInfo();
      channelSource.enqueue(tActivity);
    }
    threadContext.removeCurrentTransactionActivity();
    return result;
  }

}
