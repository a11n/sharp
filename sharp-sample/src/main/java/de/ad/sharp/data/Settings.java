package de.ad.sharp.data;

import de.ad.sharp.api.DefaultSharedPreference;

@DefaultSharedPreference public interface Settings {
  int getMyIntPreference();

  void setMyIntPreference(int value);

  float getMyFloatPreference();

  void setMyFloatPreference(float value);

  boolean isMyBooleanPreference();

  void setMyBooleanPreference(boolean value);

  String getMyStringPreference();

  void setMyStringPreference(String value);
}
