package org.opendatakit.smsbridge.activity;

import android.util.Log;
import org.opendatakit.smsbridge.sms.SMSMessenger;

/**
 * Needed to inject in an SMSMessenger object for testing.
 * @author sudar.sam@gmail.com
 */
public class SMSDispatcherActivityStub extends SMSDispatcherActivity {

  public static SMSMessenger SMS_MESSENGER = null;

  public static void resetState() {
    SMS_MESSENGER = null;
  }

  @Override
  protected SMSMessenger createSMSMessenger() {
    return SMS_MESSENGER;
  }
}
