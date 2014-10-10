package org.opendatakit.smsbridge.sms;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.smsbridge.util.AndroidIntentKeys;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sudar.sam@gmail.com
 */
@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SMSMessengerTest {

  SMSMessenger messenger;

  /** The message body used to construct the message. */
  protected String messageBody;

  /** The list of phone numbers used to construct the test messenger. */
  protected List<String> targetNumbers;

  Activity activity;

  @Before
  public void before() {

    this.messageBody = "This is the test SMS message body.";

    List<String> modifiableNumbers = new ArrayList<String>();
    modifiableNumbers.add("3605551234");

    this.targetNumbers = Collections.unmodifiableList(modifiableNumbers);

    this.activity = Robolectric.setupActivity(Activity.class);

    this.messenger = new SMSMessenger(this.messageBody, this.targetNumbers);

  }

  @After
  public void after() {
    // Don't leak state
    this.messageBody = null;
    this.targetNumbers = null;

    this.messenger = null;

  }

  @Test
  public void sendSingleMessageViaIntentHelperLaunchesIntent() {

    String phoneNumber = "3605558888";

    this.messenger.sendSingleMessageViaIntentHelper(
        this.activity,
        phoneNumber);

    Intent launchedIntent =
        Robolectric.shadowOf(this.activity).peekNextStartedActivity();

    assertThat(launchedIntent)
        .hasAction(Intent.ACTION_VIEW)
        .hasData(Uri.parse("sms:" + phoneNumber))
        .hasExtra(AndroidIntentKeys.SMS_BODY);

    String messageBodyInBundle =
        launchedIntent.getStringExtra(AndroidIntentKeys.SMS_BODY);

    assertThat(messageBodyInBundle).isEqualTo(this.messageBody);

  }

  @Test
  public void sendSMSViaIntentCallsCorrectHelper() {
    this.messenger = spy(this.messenger);

    doNothing()
        .when(this.messenger)
        .sendSingleMessageViaIntentHelper(this.activity, this.messageBody);

    this.messenger.sendSMSViaIntent(this.activity);

    verify(this.messenger, times(1))
        .sendSingleMessageViaIntentHelper(
            this.activity,
            this.targetNumbers.get(0));

  }

}
