package de.ad.sharp.util;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class FloatEditTextPreference extends EditTextPreference {

  public FloatEditTextPreference(Context context) {
    super(context);
  }

  public FloatEditTextPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public FloatEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override protected String getPersistedString(String defaultReturnValue) {
    return String.valueOf(getPersistedFloat(-1));
  }

  @Override protected boolean persistString(String value) {
    return persistFloat(Float.valueOf(value));
  }
}
