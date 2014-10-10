package org.opendatakit.smsbridge.util;

import static org.fest.assertions.api.Assertions.assertThat;

import android.os.Bundle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * @author sudar.sam@gmail.com
 */
@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class BundleUtilTest {

  Bundle bundle;

  @Before
  public void before() {
    this.bundle = new Bundle();
  }

  @After
  public void after() {
    // make sure we don't leak state
    this.bundle = null;
  }

  @Test
  public void putsAndGetsRequireConfirmation() {

    boolean target = true;

    BundleUtil.putRequireConfirmationInBundle(this.bundle, target);

    boolean actual = BundleUtil.getRequireConfirmationFromBundle(
        this.bundle,
        true);

    assertThat(actual).isEqualTo(target);

  }

  @Test
  public void putsAndGetsDeleteAfterSending() {

    boolean target = true;

    BundleUtil.putDeleteAfterSendingInBundle(this.bundle, target);

    boolean actual = BundleUtil.getDeleteAfterSendingFromBundle(
        this.bundle,
        true);

    assertThat(actual).isEqualTo(target);

  }

  @Test
  public void putsAndGetsMessageBody() {
    String msgBody = "This is a lovely message!";

    BundleUtil.putMessageBodyInBundle(this.bundle, msgBody);

    String actual = BundleUtil.getMessageBodyFromBundle(this.bundle, true);

    assertThat(actual).isEqualTo(msgBody);
  }

  @Test
  public void putsAndGetsMessageBodyUnicode() {
    String msgBody = "Testing «ταБЬℓσ»: 1<2 & 4+1>3, now 20% off!";

    BundleUtil.putMessageBodyInBundle(this.bundle, msgBody);

    String actual = BundleUtil.getMessageBodyFromBundle(this.bundle, true);

    assertThat(actual).isEqualTo(msgBody);
  }

  @Test
  public void putsAndGetsPhoneNumber() {

    String phoneNumber = "360555444";

    BundleUtil.putPhoneNumberInBundle(this.bundle, phoneNumber);

    String actual = BundleUtil.getPhoneNumberFromBundle(this.bundle, true);

    assertThat(actual).isEqualTo(phoneNumber);

  }

  @Test
  public void putsAndGetsPhoneNumberArray() {

    String[] phoneNumbers = new String[] {
        "360555333",
        "206555222"
    };

    BundleUtil.putPhoneNumberArrayInBundle(this.bundle, phoneNumbers);

    String[] actual = BundleUtil.getPhoneNumbersFromBundle(this.bundle, true);

    assertThat(actual).isEqualTo(phoneNumbers);

  }

}
