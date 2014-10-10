package org.opendatakit.smsbridge.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.opendatakit.R;
import org.opendatakit.smsbridge.sms.SMSMessenger;
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
        SMSMessenger messenger = createSMSMessenger();
        messenger.sendSMSViaIntent(WelcomeActivity.this);
      }
    });

    this.mSendWithoutConfirmation.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SMSMessenger messenger = createSMSMessenger();
        messenger.sendSMSViaManager();
      }
    });

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
