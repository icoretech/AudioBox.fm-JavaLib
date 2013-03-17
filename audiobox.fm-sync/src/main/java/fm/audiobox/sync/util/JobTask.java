package fm.audiobox.sync.util;

import java.util.Observable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.observables.Event;

public abstract class JobTask extends Observable implements Runnable {

  protected Logger log;
  
  private Future<?> referencedTask;
  private String name;
  private boolean stopped = false;
  

  public JobTask(String name) {
    this.name = name;
    log = LoggerFactory.getLogger( "Job (" + this.name + ")" );
  }
  
  public boolean isCancelled() {
    return this.stopped || this.referencedTask.isCancelled();
  }
  
  public boolean isCompleted() {
    return this.referencedTask.isDone() && !isCancelled();
  }
  
  protected void setReferencedTask(Future<?> future) {
    this.referencedTask = future;
  }
  
  protected Future<?> getReferencedTask() {
    return this.referencedTask;
  }
  
  
  public String getName() {
    return this.name;
  }
  
  public void stop() {
    this.stopped = true;
    if ( this.referencedTask != null ) {
      this.referencedTask.cancel( true );
    }
    log.info("Job has been stopped");
  }
  
  public final void run(){
    
    log.debug("Init job");
    
    if ( ! this.start() ) {
      log.info("Job will not start");
      this.setChanged();
      this.fireEvent(Event.States.START_LOADING, null);
      
      this.end(null);
      
      this.setChanged();
      this.fireEvent(Event.States.END_LOADING, null);
      
      return;
    }
    
    this.setChanged();
    this.fireEvent(Event.States.START_LOADING, null);
    
    long start = System.currentTimeMillis();
    log.debug("Starting execution");
    Object result = this.doTask();
    log.debug("Ending execution in: " + (System.currentTimeMillis() - start) + "ms");
    
    this.end(result);
    
    this.setChanged();
    this.fireEvent(Event.States.END_LOADING, result);
    
  }
  
  
  protected void execute() {
    FutureTask<Object> ft = new FutureTask<Object>((Runnable) this, new Object());
    this.setReferencedTask( ft );
    ft.run();
  }
  
  protected void fireEvent(Event.States state, Object source) {
    this.setChanged();
    Event.fireEvent(this, state, source);
  }
  
  
  public abstract Object doTask();
  public abstract boolean start();
  public abstract void end(Object result);
  
}
