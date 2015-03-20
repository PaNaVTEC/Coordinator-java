package me.panavtec.coordinator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CoordinatorTest {

  private static final int AN_ACTION = 1;
  private static final int ANOTHER_ACTION = 2;

  Coordinator coordinator;

  @Mock Runnable coordinatorComplete;
  @Mock Runnable completeAction;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.coordinator = new Coordinator(1, coordinatorComplete, AN_ACTION, ANOTHER_ACTION);
  }

  @Test public void completeCalled() {
    verifyComplete();
    Mockito.verifyNoMoreInteractions(coordinatorComplete);
  }

  @Test public void notCalledComplete() {
    coordinator.completeAction(AN_ACTION);
    Mockito.verifyNoMoreInteractions(coordinatorComplete);
  }

  @Test public void doWhenTest() {
    coordinator.doWhen(AN_ACTION, completeAction);
    coordinator.completeAction(AN_ACTION);
    Mockito.verify(completeAction).run();
  }

  private void verifyComplete() {
    coordinator.completeAction(AN_ACTION);
    coordinator.completeAction(ANOTHER_ACTION);
    Mockito.verify(coordinatorComplete).run();
  }

}
