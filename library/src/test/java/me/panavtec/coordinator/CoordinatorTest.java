package me.panavtec.coordinator;

import me.panavtec.coordinator.listeners.CoordinatorComplete;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CoordinatorTest {

  private static final int AN_ACTION = 1;
  private static final int ANOTHER_ACTION = 2;
  
  private Coordinator coordinator;
  @Mock private CoordinatorComplete coordinatorComplete;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.coordinator = new Coordinator(coordinatorComplete, AN_ACTION, ANOTHER_ACTION);
  }

  @Test public void completeCalled() {
    coordinator.completeAction(AN_ACTION);
    coordinator.completeAction(ANOTHER_ACTION);
    Mockito.verify(coordinatorComplete).onCoordinatorComplete();
  }

  @Test public void notCalledComplete() {
    coordinator.completeAction(AN_ACTION);
    Mockito.verifyNoMoreInteractions(coordinatorComplete);
  }
}
