package org.opendatakit.smsbridge.contract;

/**
 * Intent keys that the sms bridge interprets.
 * @author sudar.sam@gmail.com
 */
public class IntentKeys {

  private static final String PREFIX = "org.opendatakit.smsbridge.";

  /**
   * Boolean. True if the user must confirm the message is sent,
   * false if user confirmation is not required.
   */
  public static final String REQUIRE_CONFIRMATION =
      getNameSpacedKey("require_confirmation");

  /**
   * Boolean. True if the message will be deleted from the device after
   * sending.
   */
  public static final String DELETE_AFTER_SENDING =
      getNameSpacedKey("delete_after_sending");

  /**
   * String. The body of the message.
   */
  public static final String MESSAGE_BODY = getNameSpacedKey("message_body");

  /**
   * String. The phone number.
   */
  public static final String PHONE_NUMBER = getNameSpacedKey("phone_number");

  /**
   * String[]. A list of phone numbers to send the message to.
   */
  public static final String PHONE_NUMBER_ARRAY =
      getNameSpacedKey("phone_number_array");

  private static String getNameSpacedKey(String key) {
    return PREFIX + key;
  }



}
