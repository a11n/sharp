package de.ad.sharp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.ad.sharp.R;
import de.ad.sharp.data.Settings;
import de.ad.sharp.api.SharP;

public class MainActivity extends AppCompatActivity {

  private TextView tvIntPreference;
  private TextView tvFloatPreference;
  private TextView tvBooleanPreference;
  private TextView tvStringPreference;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initViews();

    Settings settings = SharP.getInstance(this, Settings.class);

    tvIntPreference.setText(String.valueOf(settings.getMyIntPreference()));
    tvFloatPreference.setText(String.valueOf(settings.getMyFloatPreference()));
    tvBooleanPreference.setText(String.valueOf(settings.isMyBooleanPreference()));
    tvStringPreference.setText(settings.getMyStringPreference());


  }

  private void initViews() {
    tvIntPreference = (TextView) findViewById(R.id.tvIntPreference);
    tvFloatPreference = (TextView) findViewById(R.id.tvFloatPreference);
    tvBooleanPreference = (TextView) findViewById(R.id.tvBooleanPreference);
    tvStringPreference = (TextView) findViewById(R.id.tvStringPreference);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      startActivity(new Intent(this, SettingsActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
