package me.panavtec.coordinator;

import me.panavtec.coordinator.listeners.CoordinatorComplete;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoordinatorTest implements CoordinatorComplete {
  
  private static final int AN_ACTION = 1;
  private static final int ANOTHER_ACTION = 2;
  private Coordinator coordinator;
  
  @Before public void setUp() {
    this.coordinator = new Coordinator(this, AN_ACTION, ANOTHER_ACTION);
  }
  
  @Test public void testCoordinatorWithTwoActions() {
    coordinator.completeAction(AN_ACTION);
    coordinator.completeAction(ANOTHER_ACTION);
    Assert.fail("Completed bad");
  }

  @Override public void onCoordinatorComplete() {
    
  }
}
