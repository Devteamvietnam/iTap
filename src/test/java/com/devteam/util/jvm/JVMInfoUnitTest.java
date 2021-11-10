package com.devteam.util.jvm;

import com.devteam.util.dataformat.DataSerializer;
import org.junit.jupiter.api.Test;

public class JVMInfoUnitTest {
  @Test
  public void test() {
    JVMInfo jvmInfo = new JVMInfo().init();
    System.out.println(DataSerializer.JSON.toString(jvmInfo.getMemoryInfo()));
  }
}
