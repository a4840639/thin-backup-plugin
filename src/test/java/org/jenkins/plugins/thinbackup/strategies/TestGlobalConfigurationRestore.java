package org.jenkins.plugins.thinbackup.strategies;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestGlobalConfigurationRestore extends AbstractRestoreTestUtils {
  private GlobalConfiguration globalConfiguration;

  @Before
  public void setup() throws IOException {
    globalConfiguration = new GlobalConfiguration(new File(restoredTempDir));
  }

  @After
  public void tearDown() throws IOException {
    globalConfiguration = null;
  }
  
  @Test
  public void restoreAvailableXMLConfiguration() {
    globalConfiguration.restore(backuped);

    String[] restored = new File(restoredTempDir).list();
    
    // @formatter:off
    assertThat(restored, Matchers.arrayContainingInAnyOrder(
        UPDATECENTER_CONFIG, 
        NODE_CONFIG,
        SECRET_KEY, 
        IDENTITY_KEY));
    // @formatter:on
  }
  
  @Test
  public void doNotRestoreFolders() {
    backuped = new ArrayList<File>(backuped);
    backuped.add(new File("jobs", "job1"));
    globalConfiguration.restore(backuped);

    List<String> restored = Arrays.asList(new File(restoredTempDir).list());
    
    assertThat(restored, Matchers.hasItem(Matchers.not("jobs")));
  }
  
  @Test
  public void overrideConfigurationsAllreadyExists() throws IOException {
    new File(restoredTempDir, UPDATECENTER_CONFIG).createNewFile();
    
    globalConfiguration.restore(backuped);    

    List<String> restored = Arrays.asList(new File(restoredTempDir).list());
    
    assertThat(restored, Matchers.hasItem(Matchers.not("jobs")));
  }
  
  @Test @Ignore
  public void missingFileWritePermission() {
    // TODO: find out how to simulate a missing file permission
  }
}
