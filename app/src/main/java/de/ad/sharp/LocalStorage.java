package de.ad.sharp;

import de.ad.sharp.api.SharedPreference;

@SharedPreference public interface LocalStorage {
  String getStringPreference();
  void setStringPreference(String preference);
}
