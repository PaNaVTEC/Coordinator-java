import me.panavtec.coordinator.Coordinator;
import me.panavtec.coordinator.listeners.CoordinatorComplete;
import me.panavtec.coordinator.qualifiers.Action;

public class Main {

  @Action private static final int ACTION = 1;

  public static void main(String[] args) {
    Coordinator c = new Coordinator(new CoordinatorComplete() {
      @Override public void onCoordinatorComplete() {
        System.out.println("Completed coordinator!");
      }
    }, ACTION);
    c.completeAction(ACTION);
  }
}
