package org.opendatakit.smsbridge.activity;

import org.opendatakit.smsbridge.sms.SMSMessenger;

/**
 * @author sudar.sam@gmail.com
 */
public class WelcomeActivityStub extends WelcomeActivity {

  public static SMSMessenger SMS_MESSENGER = null;

  public static boolean USE_INJECTED_DEFAULT = true;

  /**
   * Use the injected SMS manager rather than the super implementation.
   */
  public static boolean USE_INJECTED = USE_INJECTED_DEFAULT;

  public static void resetState() {
    SMS_MESSENGER = null;
    USE_INJECTED = USE_INJECTED_DEFAULT;
  }

  @Override
  protected SMSMessenger createSMSMessenger() {
    if (USE_INJECTED) {
      return SMS_MESSENGER;
    } else {
      return super.createSMSMessenger();
    }
  }
}
