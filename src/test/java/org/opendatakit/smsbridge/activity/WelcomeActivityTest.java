package org.opendatakit.smsbridge.activity;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.smsbridge.sms.SMSMessenger;
import org.opendatakit.smsbridge.util.BundleUtil;
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
  public void withConfirmationLaunchesCorrectIntent() {

    ComponentName targetComponent = new ComponentName(
        "org.opendatakit.smsbridge",
        "org.opendatakit.smsbridge.activity.SMSDispatcherActivity");

    String messageBody = "fancy message body";
    String phoneNumber = "3605557777";

    Bundle target = this.getBundleForParams(
        true,
        false,
        messageBody,
        phoneNumber,
        null);

    this.activity.mEnterMessageBody.setText(messageBody);
    this.activity.mEnterPhoneNumber.setText(phoneNumber);

    this.activity.mSendWithConfirmation.performClick();

    Intent launched = Robolectric.shadowOf(this.activity)
        .peekNextStartedActivity();

    assertThat(launched)
        .isNotNull()
        .hasComponent(targetComponent);

    assertThat(launched.getExtras()).isEqualTo(target);

  }

  @Test
  public void getBundleCorrectWithSingleNumber() {

    String phoneNumber = "3605558888";
    this.activity.mEnterPhoneNumber.setText(phoneNumber);

    String messageBody = "hello hello";
    this.activity.mEnterMessageBody.setText(messageBody);

    boolean requireConfirmation = true;

    Bundle target = this.getBundleForParams(
        requireConfirmation,
        false,  // always false for now
        messageBody,
        phoneNumber,
        null);

    Bundle actual = this.activity.getBundleFromFields(
        requireConfirmation,
        false);

    this.helperBundleAssert(actual, target);

  }

  @Test
  public void getBundleCorrectWithNumberArray() {

    String phoneNumbers = "3605559999, 2065551234";
    this.activity.mEnterPhoneNumber.setText(phoneNumbers);

    String messageBody = "tea or skyrim";
    this.activity.mEnterMessageBody.setText(messageBody);

    boolean requireConfirmation = false;

    Bundle target = this.getBundleForParams(
        requireConfirmation,
        false,
        messageBody,
        null,
        new String[] { "3605559999", "2065551234" });

    Bundle actual = this.activity.getBundleFromFields(
        requireConfirmation,
        false);

    this.helperBundleAssert(actual, target);

  }

  @Test
  public void getBundleCorrectWithNoText() {

    String phoneNumber = "";
    this.activity.mEnterPhoneNumber.setText(phoneNumber);

    String messageBody = "hello hello";
    this.activity.mEnterMessageBody.setText(messageBody);

    boolean requireConfirmation = true;

    Bundle target = this.getBundleForParams(
        requireConfirmation,
        false,  // always false for now
        messageBody,
        phoneNumber,
        null);

    Bundle actual = this.activity.getBundleFromFields(
        requireConfirmation,
        false);

    assertThat(actual).isEqualTo(target);

  }

  @Test
  public void withoutConfirmationLaunchesCorrectIntent() {

    ComponentName targetComponent = new ComponentName(
        "org.opendatakit.smsbridge",
        "org.opendatakit.smsbridge.activity.SMSDispatcherActivity");

    String messageBody = "fancy message body";
    String phoneNumber = "3605557777";

    Bundle target = this.getBundleForParams(
        false,
        false,
        messageBody,
        phoneNumber,
        null);

    this.activity.mEnterMessageBody.setText(messageBody);
    this.activity.mEnterPhoneNumber.setText(phoneNumber);

    this.activity.mSendWithoutConfirmation.performClick();

    Intent launched = Robolectric.shadowOf(this.activity)
        .peekNextStartedActivity();

    assertThat(launched)
        .isNotNull()
        .hasComponent(targetComponent);

    Bundle actualBundle = launched.getExtras();

    this.helperBundleAssert(actualBundle, target);

  }

  /**
   * Assert that our Bundle is the same. Normal fest assertion fails due to the
   * array.
   * @param actual
   * @param target
   */
  protected void helperBundleAssert(Bundle actual, Bundle target) {

    // Annoyingly, this doesn't seem to do Array.equals correctly.

    boolean requireConfirmation =
        BundleUtil.getRequireConfirmationFromBundle(actual, true);
    String messageBody = BundleUtil.getMessageBodyFromBundle(actual, true);
    String phoneNumber = BundleUtil.getPhoneNumberFromBundle(actual, true);
    String[] numbers = BundleUtil.getPhoneNumbersFromBundle(actual, true);

    assertThat(BundleUtil.getRequireConfirmationFromBundle(target, true))
        .isEqualTo(requireConfirmation);

    assertThat(BundleUtil.getDeleteAfterSendingFromBundle(target, true))
        .isFalse();

    assertThat(BundleUtil.getMessageBodyFromBundle(target, true))
        .isEqualTo(messageBody);

    assertThat(BundleUtil.getPhoneNumberFromBundle(target, true))
        .isEqualTo(phoneNumber);

    assertThat(BundleUtil.getPhoneNumbersFromBundle(target, true))
        .isEqualTo(numbers);
  }

  /**
   * Helper to construct a Bundle.
   * @param requireConfirmation
   * @param deleteMessage
   * @param messageBody
   * @param phoneNumber
   * @param phoneNumbers
   * @return
   */
  protected Bundle getBundleForParams(
      boolean requireConfirmation,
      boolean deleteMessage,
      String messageBody,
      String phoneNumber,
      String[] phoneNumbers) {

    Bundle result = new Bundle();

    BundleUtil.putRequireConfirmationInBundle(result, requireConfirmation);
    BundleUtil.putDeleteAfterSendingInBundle(result, deleteMessage);
    BundleUtil.putMessageBodyInBundle(result, messageBody);
    BundleUtil.putPhoneNumberInBundle(result, phoneNumber);
    BundleUtil.putPhoneNumberArrayInBundle(result, phoneNumbers);

    return result;

  }

}
