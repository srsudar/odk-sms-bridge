package org.opendatakit.smsbridge.activity;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.smsbridge.R;
import org.opendatakit.smsbridge.sms.SMSMessenger;
import org.opendatakit.smsbridge.util.TestUtil;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sudar.sam@gmail.com
 */
@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SMSDispatcherActivityTest {

  ActivityController<SMSDispatcherActivityStub> activityController;

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
        Robolectric.buildActivity(SMSDispatcherActivityStub.class)
            .withIntent(intent);

  }

  @After
  public void after() {
    SMSDispatcherActivityStub.resetState();
    this.activityController = null;
  }

  @Test
  public void initializesNonNull() {

    this.initializeWithValidParameters();

    Activity activity = this.activityController.create().get();

    assertThat(activity).isNotNull();

  }

  @Test
  public void reclaimsValuesFromIntent() {

    boolean requireConfirmation = true;
    boolean deleteMessage = true;
    String msgBody = "this is a message body";
    String phoneNumber = "first number";
    String[] phoneNumbers = new String[] { "numberOne", "numberTwo"};

    this.initializeWithParamters(
        requireConfirmation,
        deleteMessage,
        msgBody,
        phoneNumber,
        phoneNumbers);

    SMSDispatcherActivity activity = this.activityController.create().get();

    assertThat(activity.mRequireConfirmation).isEqualTo(requireConfirmation);
    assertThat(activity.mDeleteAfterSending).isEqualTo(deleteMessage);
    assertThat(activity.mMessageBody).isEqualTo(msgBody);
    assertThat(activity.mPhoneNumber).isEqualTo(phoneNumber);
    assertThat(activity.mPhoneNumbers).isEqualTo(phoneNumbers);

    List<String> reclaimedNumbers = new ArrayList<String>();
    reclaimedNumbers.add(phoneNumber);
    for (String number : phoneNumbers) {
      reclaimedNumbers.add(number);
    }

    assertThat(activity.mTargetNumbers)
        .hasSameSizeAs(reclaimedNumbers)
        .containsAll(reclaimedNumbers);

  }

  @Test
  public void noMessageBodySpecifiedNullTrue() {
    this.initializeWithMessage(null);

    SMSDispatcherActivity activity = this.activityController.create().get();

    assertThat(activity.noMessageBodySpecified()).isTrue();
  }

  @Test
  public void noMessageBodySpecifiedEmptyTrue() {
    this.initializeWithMessage("");

    SMSDispatcherActivity activity = this.activityController.create().get();

    assertThat(activity.noMessageBodySpecified()).isTrue();
  }

  @Test
  public void noMessageBodySpecifiedValidFalse() {
    this.initializeWithMessage("this is a body");

    SMSDispatcherActivity activity = this.activityController.create().get();

    assertThat(activity.noMessageBodySpecified()).isFalse();
  }

  @Test
  public void noNumbersSaysNoSpecified() {
    this.initializeWithPhoneNumber(null);

    SMSDispatcherActivity activity = this.activityController.create().get();

    assertThat(activity.noPhoneNumbersSpecified()).isTrue();
  }

  @Test
  public void numbersSpecifiedCorrect() {
    this.initializeWithPhoneNumber("phone_number");

    SMSDispatcherActivity activity = this.activityController.create().get();

    assertThat(activity.noPhoneNumbersSpecified()).isFalse();
  }

  @Test
  public void assertValidStateFinishesAndToastsIfNoMessageBodySpecified() {

    this.initializeWithMessage("");

    SMSDispatcherActivity activity = this.activityController
        .create()
        .get();

    boolean isValid = activity.assertValidState();

    assertThat(isValid).isFalse();

    assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(
        Robolectric.application.getString(R.string.no_message_body));

  }

  @Test
  public void assertValidStateTrueIfValid() {

    this.initializeWithValidParameters();

    SMSDispatcherActivity activity = this.activityController
        .create()
        .get();

    boolean isValid = activity.assertValidState();

    assertThat(isValid).isTrue();

  }

  @Test
  public void assertValidStateReturnsFalseAndToastsIfNoPhoneNumberSpecified() {

    this.initializeWithPhoneNumber(null);

    SMSDispatcherActivityStub activity = this.activityController
        .create()
        .get();

    boolean isValid = activity.assertValidState();

    assertThat(isValid).isFalse();

    assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(
        Robolectric.application.getString(R.string.no_phone_number));

  }

  @Test
  public void onStartSendsViaIntentAndFinishes() {

    this.initializeWithParamters(
        true,  // we want to send via intent
        true,
        "message body"
        ,"3605551234",
        null);

    SMSMessenger messengerMock = mock(SMSMessenger.class);

    SMSDispatcherActivityStub.SMS_MESSENGER = messengerMock;

    SMSDispatcherActivityStub activity = this.activityController
        .create()
        .start()
        .get();

    assertThat(activity).isFinishing();

    verify(messengerMock, times(1)).sendSMSViaIntent(activity);

  }

  @Test
  public void onStartSendsViaManagerAndFinishes() {

    SMSMessenger messengerMock = mock(SMSMessenger.class);

    SMSDispatcherActivityStub.SMS_MESSENGER = messengerMock;

    this.initializeWithParamters(
        false,  // we want to go straight to the manager
        true,
        "message body"
        ,"3605551234",
        null);

    SMSDispatcherActivityStub activity = this.activityController
        .create()
        .start()
        .get();

    assertThat(activity).isFinishing();

    verify(messengerMock, times(1)).sendSMSViaManager();

  }

  @Test
  public void deletesMessage() {

  }

  /**
   * Set up the activity controller with all valid parameters and the given
   * message.
   * @param message
   */
  protected void initializeWithMessage(String message) {

    this.initializeWithParamters(
        true,
        true,
        message,
        "phone_number",
        new String[] { "number_one", "number_two"});

  }

  protected void initializeWithPhoneNumber(String phoneNumber) {

    this.initializeWithParamters(
        true,
        true,
        "a message",
        phoneNumber,
        null);

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
