package com.devteam.util.jvm;

import java.lang.management.GarbageCollectorMXBean;

import net.datatp.util.text.DateUtil;
import net.datatp.util.text.StringUtil;

public class GarbageCollectorInfo {
  
  private String name;
  private String collectionCount;
  private String collectionTime;
  private String poolNames;

  public GarbageCollectorInfo() {}
  
  public GarbageCollectorInfo(GarbageCollectorMXBean gcbean) {
    name = gcbean.getName();
    collectionCount = Long.toString(gcbean.getCollectionCount());
    collectionTime = DateUtil.asHumanReadable(gcbean.getCollectionTime());
    poolNames = StringUtil.joinStringArray(gcbean.getMemoryPoolNames(), "|");
  }
  
  public String getName() { return name; }
  public void setName(String name) { this.name = name;}

  public String getCollectionCount() { return collectionCount; }
  public void setCollectionCount(String collectionCount) { this.collectionCount = collectionCount; }

  public String getCollectionTime() { return collectionTime; }
  public void setCollectionTime(String collectionTime) { this.collectionTime = collectionTime;}

  public String getPoolNames() { return poolNames; }
  public void setPoolNames(String poolNames) { this.poolNames = poolNames; }
}
