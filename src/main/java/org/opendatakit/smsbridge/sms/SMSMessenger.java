package org.opendatakit.smsbridge.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import org.opendatakit.smsbridge.util.AndroidIntentKeys;
import org.opendatakit.smsbridge.util.RequestCodes;

import java.util.List;

/**
 * Sends SMS messages.
 * @author sudar.sam@gmail.com
 */
public class SMSMessenger {

  private static final String TAG = SMSMessenger.class.getSimpleName();

  /**
   * The prefix of a uri that indicates an sms.
   */
  private static final String URI_PREFIX = "sms:";

  protected String mMessageBody;
  protected List<String> mTargetNumbers;

  public SMSMessenger(String messageBody, List<String> targetNumbers) {
    this.mMessageBody = messageBody;
    this.mTargetNumbers = targetNumbers;

    if (this.mTargetNumbers.size() > 1) {
      throw new IllegalArgumentException(
          "more than one target number is not yet supported");
    }

  }

  public void sendSMSViaIntent(Activity launchingActivity) {

    if (this.mTargetNumbers.size() > 1) {
      Log.e(
          TAG,
          "[sendSMSViaIntent] sending to multiple numbers is unimplemented");
    }

    // For now, don't handle the multiple number case. We might have to
    // take it off the UI thread.
    this.sendSingleMessageViaIntentHelper(
        launchingActivity,
        this.mTargetNumbers.get(0));

  }

  protected void sendSingleMessageViaIntentHelper(
      Activity launchingActivity,
      String phoneNumber) {

    Uri dataUri = Uri.parse(URI_PREFIX + phoneNumber);

    Intent intent = new Intent(Intent.ACTION_VIEW, dataUri);

    intent.putExtra(AndroidIntentKeys.SMS_BODY, this.mMessageBody);

    launchingActivity.startActivityForResult(
        intent,
        RequestCodes.SEND_SMS_VIA_INTENT);

  }

  public void sendSMSViaManager() {

    SmsManager defaultManager = SmsManager.getDefault();

    if (this.mTargetNumbers.size() > 1) {
      Log.e(
          TAG,
          "[sendSMSViaManager] sending to multiple numbers is unimplemented");
    }

    this.sendSingleMessageViaManagerHelper(
        defaultManager,
        this.mTargetNumbers.get(0));

  }

  protected void sendSingleMessageViaManagerHelper(
      SmsManager manager,
      String phoneNumber) {

    manager.sendTextMessage(
        phoneNumber,
        null,
        this.mMessageBody,
        null,
        null);

  }

  @Override
  public String toString() {
    return "SMSMessenger{" +
        "mMessageBody='" + mMessageBody + '\'' +
        ", mTargetNumbers=" + mTargetNumbers +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SMSMessenger that = (SMSMessenger) o;

    if (mMessageBody != null ? !mMessageBody.equals(that.mMessageBody) : that.mMessageBody != null)
      return false;
    if (mTargetNumbers != null ? !mTargetNumbers.equals(that.mTargetNumbers) : that.mTargetNumbers != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = mMessageBody != null ? mMessageBody.hashCode() : 0;
    result = 31 * result + (mTargetNumbers != null ? mTargetNumbers.hashCode() : 0);
    return result;
  }
}
