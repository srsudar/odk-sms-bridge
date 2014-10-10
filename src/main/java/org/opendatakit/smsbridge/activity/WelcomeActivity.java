package org.opendatakit.smsbridge.activity;

import android.app.Activity;
import android.os.Bundle;
import org.opendatakit.R;

public class WelcomeActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
  }
}
