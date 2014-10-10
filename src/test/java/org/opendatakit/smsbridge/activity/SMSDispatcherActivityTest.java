package org.opendatakit.smsbridge.activity;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.fest.assertions.api.android.widget.ToastAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.R;
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
        .start()
        .get();

    assertThat(activity).isFinishing();

    assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(
        Robolectric.application.getString(R.string.no_message_body));

  }

  @Test
  public void assertValidStateFinishesAndToastsIfNoPhoneNumberSpecified() {

    this.initializeWithPhoneNumber(null);

    SMSDispatcherActivity activity = this.activityController
        .create()
        .start()
        .get();

    assertThat(activity).isFinishing();

    assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(
        Robolectric.application.getString(R.string.no_phone_number));

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
