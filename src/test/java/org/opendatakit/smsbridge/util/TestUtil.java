package org.opendatakit.smsbridge.util;

import android.os.Bundle;

/**
 * @author sudar.sam@gmail.com
 */
public class TestUtil {

  public static Bundle getBundleWithParameters(
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
