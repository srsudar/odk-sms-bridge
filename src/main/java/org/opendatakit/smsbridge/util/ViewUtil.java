package org.opendatakit.smsbridge.util;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author sudar.sam@gmail.com
 */
public class ViewUtil {

  public static EditText findEditText(View rootView, int id) {
    EditText result = (EditText) rootView.findViewById(id);
    return result;
  }

  public static Button findButton(View rootView, int id) {
    Button result = (Button) rootView.findViewById(id);
    return result;
  }

}
