package org.opendatakit.smsbridge.util;

import android.os.Bundle;
import org.opendatakit.smsbridge.contract.IntentKeys;

/**
 * @author sudar.sam@gmail.com
 */
public class BundleUtil {

  public static void putRequireConfirmationInBundle(
      Bundle bundle,
      boolean requireConfirmation) {
    bundle.putBoolean(IntentKeys.REQUIRE_CONFIRMATION, requireConfirmation);
  }

  public static void putDeleteAfterSendingInBundle(
      Bundle bundle,
      boolean delete) {
    bundle.putBoolean(IntentKeys.DELETE_AFTER_SENDING, delete);
  }

  public static void putMessageBodyInBundle(
      Bundle bundle,
      String messageBody) {
    bundle.putString(IntentKeys.MESSAGE_BODY, messageBody);
  }

  public static void putPhoneNumberInBundle(
      Bundle bundle,
      String phoneNumber) {
    bundle.putString(IntentKeys.PHONE_NUMBER, phoneNumber);
  }

  public static void putPhoneNumberArrayInBundle(
      Bundle bundle,
      String[] phoneNumbers) {
    bundle.putStringArray(IntentKeys.PHONE_NUMBER_ARRAY, phoneNumbers);
  }

  public static boolean getRequireConfirmationFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    String key = IntentKeys.REQUIRE_CONFIRMATION;
    doThrowIfNotPresent(bundle, key, throwIfNotPresent);
    boolean result = bundle.getBoolean(key);
    return result;
  }

  public static boolean getDeleteAfterSendingFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    String key = IntentKeys.DELETE_AFTER_SENDING;
    doThrowIfNotPresent(bundle, key, throwIfNotPresent);
    boolean result = bundle.getBoolean(key);
    return result;
  }

  public static String getMessageBodyFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    String key = IntentKeys.MESSAGE_BODY;
    doThrowIfNotPresent(bundle, key, throwIfNotPresent);
    String result = bundle.getString(key);
    return result;
  }

  public static String getPhoneNumberFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    String key = IntentKeys.PHONE_NUMBER;
    doThrowIfNotPresent(bundle, key, throwIfNotPresent);
    String result = bundle.getString(key);
    return result;
  }

  public static String[] getPhoneNumbersFromBundle(
      Bundle bundle,
      boolean throwIfNotPresent) {
    String key = IntentKeys.PHONE_NUMBER_ARRAY;
    doThrowIfNotPresent(bundle, key, throwIfNotPresent);
    String[] result = bundle.getStringArray(key);
    return result;
  }

  /**
   * If doThrow is true, throw {@link java.lang.IllegalStateException} if
   * bundle does not contain key.
   * @param bundle
   * @param key
   * @param doThrow
   */
  protected static void doThrowIfNotPresent(
      Bundle bundle,
      String key,
      boolean doThrow) {
    if (doThrow) {
      if (!bundle.containsKey(key)) {
        throw new IllegalStateException("Bundle must contain: " + key);
      }
    }
  }

}
