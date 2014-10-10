package org.opendatakit.smsbridge.activity;

import android.app.Activity;
import android.os.Bundle;
import org.opendatakit.R;
import org.opendatakit.smsbridge.util.BundleUtil;
import org.opendatakit.smsbridge.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity is responsible for sending an SMS. It does not have a UI,
 * instead finishing as soon as it has sent the message. It serves as an entry
 * point into the app.
 * @author sudar.sam@gmail.com
 */
public class SMSDispatcherActivity extends Activity {

  protected boolean mRequireConfirmation;
  protected boolean mDeleteAfterSending;
  protected String mMessageBody;
  protected String mPhoneNumber;
  protected String[] mPhoneNumbers;
  protected List<String> mTargetNumbers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle extras = this.getIntent().getExtras();

    this.intializeFromBundle(extras);

  }

  @Override
  protected void onStart() {
    super.onStart();

    // Do any calls to finish() here so that we have an easier time testing--
    // i.e. we can still get at the activity after calls to onCreate(). This is
    // ok, since it is still not visible to the user by this point.

    this.assertValidState();

  }

  /**
   * Initialize the activity's state from the bundle.
   * @param bundle
   */
  protected void intializeFromBundle(Bundle bundle) {

    this.mRequireConfirmation = BundleUtil.getRequireConfirmationFromBundle(
        bundle,
        true);

    this.mDeleteAfterSending = BundleUtil.getDeleteAfterSendingFromBundle(
        bundle,
        true);

    this.mMessageBody = BundleUtil.getMessageBodyFromBundle(
        bundle,
        true);

    this.mPhoneNumber = BundleUtil.getPhoneNumberFromBundle(bundle, false);

    this.mPhoneNumbers = BundleUtil.getPhoneNumbersFromBundle(bundle, false);

    this.mTargetNumbers = new ArrayList<String>();

    if (this.mPhoneNumber != null) {
      this.mTargetNumbers.add(this.mPhoneNumber);
    }

    if (this.mPhoneNumbers != null) {
      for (String number : this.mPhoneNumbers) {
        this.mTargetNumbers.add(number);
      }
    }

  }

  /**
   * Asserts that the activity's state is valid. If not,
   */
  protected void assertValidState() {

    if (this.noMessageBodySpecified()) {
      ToastUtil.toastShort(this, R.string.no_message_body);
      this.finish();
    }

    if (this.noPhoneNumbersSpecified()) {
      ToastUtil.toastShort(this, R.string.no_phone_number);
      this.finish();
    }

  }

  /**
   * True if the message body is null or the empty string.
   * @return
   */
  protected boolean noMessageBodySpecified() {
    return this.mMessageBody == null || this.mMessageBody.equals("");
  }

  protected boolean noPhoneNumbersSpecified() {
    return this.mTargetNumbers.isEmpty();
  }


}
