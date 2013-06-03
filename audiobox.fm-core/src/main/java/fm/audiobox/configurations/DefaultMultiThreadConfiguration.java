package fm.audiobox.configurations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the standard configuration for using {@code multi-threaded connections}
 */
public class DefaultMultiThreadConfiguration extends DefaultConfiguration {

  private int numberOfThreads = 10;

  
  public DefaultMultiThreadConfiguration(String appName, int numberOfThreads){
    super(appName);
    this.numberOfThreads = numberOfThreads;
  }
  
  public DefaultMultiThreadConfiguration(String appName, int major, int minor, int revision, int numberOfThreads){
    super(appName, major, minor, revision);
    this.numberOfThreads = numberOfThreads;
  }

  public DefaultMultiThreadConfiguration(String appName, int major, int minor, int revision, ContentFormat requestFormat, int numberOfThreads) {
    super(appName, major, minor, revision, requestFormat);
    this.numberOfThreads = numberOfThreads;
  }


  public DefaultMultiThreadConfiguration(String appName, int major, int minor, int revision, ContentFormat requestFormat, Environments env, int numberOfThreads) {
    super(appName, major, minor, revision, requestFormat, env);
    this.numberOfThreads = numberOfThreads;
  }


  public ExecutorService getExecutor() {
    if ( this.executor == null ) {
      this.executor = Executors.newFixedThreadPool( this.numberOfThreads );
    }
    return this.executor;
  }

}
