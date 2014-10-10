package org.opendatakit.smsbridge.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author sudar.sam@gmail.com
 */
public class ToastUtil {

  public static void toastShort(
      Context context,
      int messageId) {
    Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
  }

  public static void toastShort(
      Context context,
      String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

}
