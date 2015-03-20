package me.panavtec.coordinator;

import me.panavtec.coordinator.compiler.qualifiers.Actions;
import me.panavtec.coordinator.compiler.qualifiers.CoordinatedAction;
import me.panavtec.coordinator.compiler.qualifiers.CoordinatorComplete;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CoordinatorInjectTest {

  private static final int AN_ACTION = 1;
  private static final int ANOTHER_ACTION = 2;

  @Actions({ AN_ACTION, ANOTHER_ACTION }) Coordinator coordinator;

  @Mock @CoordinatorComplete Runnable coordinatorComplete;
  @Mock @CoordinatedAction(action = AN_ACTION) Runnable completeAction;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    Coordinator.inject(this);
  }

  @Test public void coordinatorComplete() {
    coordinator.completeAction(AN_ACTION);
    coordinator.completeAction(ANOTHER_ACTION);
    Mockito.verify(coordinatorComplete).run();
    Mockito.verifyNoMoreInteractions(coordinatorComplete);
  }

  @Test public void notCalledComplete() {
    coordinator.completeAction(AN_ACTION);
    Mockito.verifyNoMoreInteractions(coordinatorComplete);
  }

  @Test public void actionComplete() {
    coordinator.completeAction(AN_ACTION);
    Mockito.verify(completeAction).run();
  }

}
