package org.opendatakit.smsbridge.activity;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.smsbridge.sms.SMSMessenger;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class WelcomeActivityTest {

  WelcomeActivityStub activity;

  @Before
  public void before() {
    this.activity = Robolectric.setupActivity(WelcomeActivityStub.class);
  }

  @After
  public void after() {
    this.activity = null;
    WelcomeActivityStub.resetState();
  }

  @Test
  public void createsSuccessfullly() throws Exception {
      assertThat(this.activity).isNotNull();
  }

  @Test
  public void withoutConfirmationCallsCorrectly() {

    SMSMessenger messengerMock = mock(SMSMessenger.class);

    WelcomeActivityStub.SMS_MESSENGER = messengerMock;

    String phoneNumber = "3605551234";
    String messageBody = "This is a fancy message body.";

    this.activity.mEnterPhoneNumber.setText(phoneNumber);
    this.activity.mEnterMessageBody.setText(messageBody);

    this.activity.mSendWithoutConfirmation.performClick();

    verify(messengerMock, times(1)).sendSMSViaManager();

  }

  @Test
  public void withConfirmationCallsCorrectly() {

    SMSMessenger messengerMock = mock(SMSMessenger.class);

    WelcomeActivityStub.SMS_MESSENGER = messengerMock;

    this.activity.mSendWithConfirmation.performClick();

    verify(messengerMock, times(1)).sendSMSViaIntent(this.activity);

  }

  @Test
  public void getEnteredPhoneNumbersCorrectSingle() {

    String phoneNumber = "3605551234 ";

    List<String> target = new ArrayList<String>();
    target.add(phoneNumber.trim());

    this.activity.mEnterPhoneNumber.setText(phoneNumber);

    List<String> actual = this.activity.getEnteredPhoneNumbers();

    assertThat(actual).isEqualTo(target);

  }

  @Test
  public void getEnteredPhoneNumbersCorrectMultiple() {

    String enteredNumbers = "3605551234, 2065554321,3605550000";

    List<String> target = new ArrayList<String>();
    target.add("3605551234");
    target.add("2065554321");
    target.add("3605550000");

    this.activity.mEnterPhoneNumber.setText(enteredNumbers);

    List<String> actual = this.activity.getEnteredPhoneNumbers();

    assertThat(actual).isEqualTo(target);

  }

  @Test
  public void getMessageBodyCorrect() {

    String messageBody = "this is a fancy message body";

    this.activity.mEnterMessageBody.setText(messageBody);

    String actual = this.activity.getEnteredMessageBody();

    assertThat(actual).isEqualTo(messageBody);

  }

  @Test
  public void smsMessengerCreatedSuccessfully() {

    // We want to use the real super implementation.
    WelcomeActivityStub.USE_INJECTED = false;

    String phoneNumber = "3605554444";
    String messageBody = "My fancy test message body";

    List<String> targetNumbers = new ArrayList<String>();
    targetNumbers.add(phoneNumber);

    this.activity.mEnterPhoneNumber.setText(phoneNumber);
    this.activity.mEnterMessageBody.setText(messageBody);

    SMSMessenger retrieved = this.activity.createSMSMessenger();

    SMSMessenger target = new SMSMessenger(messageBody, targetNumbers);

    assertThat(retrieved).isEqualTo(target);

  }

}
