package fm.audiobox.sync.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.sync.util.JobManager;
import fm.audiobox.sync.util.JobTask;

public class ThreadTest extends TestCase {

  private static Logger logger = LoggerFactory.getLogger(ThreadTest.class);
  
  @Test
  public void test4Threads() {
  
    JobManager manager = new JobManager(4);
    
    logger.info("Jobs started");
    
    for(int i = 0; i < 4; i++ ){
      manager.execute( new Task("1 Task " + (i+1) ) );
    }
    
    while( ! manager.isCompleted() ){}
    
    logger.info("------------ Jobs terminated -----------------");
  }
  
  
  @SuppressWarnings("static-access")
  public void test15ThreadsAndStop(){
    
    JobManager manager = new JobManager(4);
    
    logger.info("------------ Jobs started ----------------");
    
    for(int i = 0; i < 15; i++ ){
      manager.execute( new Task("2 Task " + (i+1) ) );
    }
    
    try {
      Thread.currentThread().sleep( 1000 );
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    manager.stop();
    logger.info("------------------- Jobs STOPPED ------------------");
    
    while( ! manager.isCompleted() ){}
    
    logger.info("------------------- Jobs terminated ------------------");
    
  }
  
  
  private class Task extends JobTask {

    public Task(String name) {
      super(name);
    }

    @SuppressWarnings("static-access")
    public Object doTask() {
      
      try {
        Thread.currentThread().sleep( 500 );
      } catch (InterruptedException e) {
        this.log.error("Interrupted exception");
      }
      
      return null;
    }

    public boolean start() {
      return true;
    }

    public void end(Object result) {
      
    }
    
  }
  
}
