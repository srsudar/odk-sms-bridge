package org.opendatakit.smsbridge.activity;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.smsbridge.util.TestUtil;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

/**
 * @author sudar.sam@gmail.com
 */
@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SMSDispatcherActivityTest {

  ActivityController<SMSDispatcherActivity> activityController;

  protected void initializeWithParamters(
      boolean requireConfirmation,
      boolean deleteMessage,
      String messageBody,
      String phoneNumber,
      String[] phoneNumbers) {

    Bundle bundle = TestUtil.getBundleWithParameters(
        requireConfirmation,
        deleteMessage,
        messageBody,
        phoneNumber,
        phoneNumbers);

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.putExtras(bundle);

    this.activityController =
        Robolectric.buildActivity(SMSDispatcherActivity.class)
            .withIntent(intent);

  }

  @Test
  public void intializesCorrectly() {

    this.initializeWithValidParameters();

    Activity activity = this.activityController.create().get();

    assertThat(activity).isNotNull();

  }

  /**
   * Initialize the {@link org.robolectric.util.ActivityController} with valid
   * parameters.
   */
  protected void initializeWithValidParameters() {

    this.initializeWithParamters(
        true,
        true,
        "some body",
        "some number",
        null);

  }

}
