package me.panavtec.coordinator.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import me.panavtec.coordinator.Coordinator;
import me.panavtec.coordinator.qualifiers.Actions;
import me.panavtec.coordinator.qualifiers.CoordinatedAction;
import me.panavtec.coordinator.qualifiers.CoordinatorComplete;

public class MainActivity extends ActionBarActivity {

  private static final int AN_ACTION = 1;
  private static final int ANOTHER_ACTION = 2;
  
  @Actions({AN_ACTION, ANOTHER_ACTION}) Coordinator coordinator;
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Coordinator.inject(this);
    setContentView(R.layout.activity_main);
    coordinator.completeAction(AN_ACTION);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        coordinator.completeAction(ANOTHER_ACTION);
      }
    }, 5000);
  }
  
  @CoordinatedAction(action = AN_ACTION) public void onCoordinatedAction() {
    Toast.makeText(MainActivity.this, "First", Toast.LENGTH_SHORT).show();
  }

  @CoordinatorComplete public void onCoordinateComplete() {
    this.runOnUiThread(new Runnable() {
      @Override public void run() {
        Toast.makeText(MainActivity.this, "Yeah!", Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
