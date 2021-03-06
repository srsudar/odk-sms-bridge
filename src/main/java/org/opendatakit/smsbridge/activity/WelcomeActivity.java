package org.opendatakit.smsbridge.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.opendatakit.smsbridge.R;
import org.opendatakit.smsbridge.sms.SMSMessenger;
import org.opendatakit.smsbridge.util.BundleUtil;
import org.opendatakit.smsbridge.util.RequestCodes;
import org.opendatakit.smsbridge.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

  protected EditText mEnterPhoneNumber;
  protected EditText mEnterMessageBody;

  protected Button mSendWithConfirmation;
  protected Button mSendWithoutConfirmation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    View contentView = this.findViewById(android.R.id.content);

    this.mEnterPhoneNumber = ViewUtil.findEditText(
        contentView,
        R.id.enter_phone_number);

    this.mEnterMessageBody = ViewUtil.findEditText(
        contentView,
        R.id.enter_message_body);

    this.mSendWithConfirmation = ViewUtil.findButton(
        contentView,
        R.id.send_sms_with_confirmation);

    this.mSendWithoutConfirmation = ViewUtil.findButton(
        contentView,
        R.id.send_sms_without_confirmation);

    this.mSendWithConfirmation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        launchIntentForDispatcher(true);
      }
    });

    this.mSendWithoutConfirmation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        launchIntentForDispatcher(false);
      }
    });

  }

  /**
   * Launch the
   * {@link org.opendatakit.smsbridge.activity.SMSDispatcherActivity}. The SMS
   * could instead be sent using the
   * {@link org.opendatakit.smsbridge.sms.SMSMessenger} directly, but the
   * point of this activity is to give users a sense of how the Intents work,
   * so we will instead do this by hand.
   * @return
   */
  protected void launchIntentForDispatcher(boolean requireConfirmation) {

    Intent intent = new Intent();

    ComponentName componentName = new ComponentName(
        "org.opendatakit.smsbridge",
        "org.opendatakit.smsbridge.activity.SMSDispatcherActivity");

    intent.setComponent(componentName);

    // always false for delete for now, since we might not be able to support
    // it.
    Bundle extras = this.getBundleFromFields(requireConfirmation, false);

    intent.putExtras(extras);

    this.startActivityForResult(
        intent,
        RequestCodes.START_DISPATCHER_ACTIVITY);

  }

  protected Bundle getBundleFromFields(
      boolean requireConfirmation,
      boolean deleteMessage) {

    List<String> phoneNumbers = this.getEnteredPhoneNumbers();
    String[] phoneNumberArray = null;
    String phoneNumber = null;

    if (phoneNumbers.size() == 0) {
      phoneNumber = "";
    } else if (phoneNumbers.size() == 1) {
      phoneNumber = phoneNumbers.get(0);
    } else {
      // > 1
      phoneNumberArray = phoneNumbers.toArray(new String[]{});
    }

    Bundle result = new Bundle();

    BundleUtil.putRequireConfirmationInBundle(result, requireConfirmation);
    BundleUtil.putDeleteAfterSendingInBundle(result, deleteMessage);
    BundleUtil.putMessageBodyInBundle(result, this.getEnteredMessageBody());
    BundleUtil.putPhoneNumberInBundle(result, phoneNumber);
    BundleUtil.putPhoneNumberArrayInBundle(result, phoneNumberArray);

    return result;

  }

  /**
   * Create a messenger object from the UI fields.
   * @return
   */
  protected SMSMessenger createSMSMessenger() {

    List<String> numbers = this.getEnteredPhoneNumbers();

    String messageBody = this.getEnteredMessageBody();

    SMSMessenger result = new SMSMessenger(messageBody, numbers);

    return result;

  }

  /**
   * Turn the comma-separated list into a list of numbers. Removes whitespace
   * from individual numbers.
   * @return
   */
  protected List<String> getEnteredPhoneNumbers() {
    String enteredNumbers = this.mEnterPhoneNumber.getText().toString();

    String[] numbersArray = enteredNumbers.split(",");

    List<String> result = new ArrayList<String>();

    for (String number : numbersArray) {
      number = number.trim();
      result.add(number);
    }

    return result;

  }

  protected String getEnteredMessageBody() {
    return this.mEnterMessageBody.getText().toString();
  }

}
