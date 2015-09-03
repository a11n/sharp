package de.ad.sharp;

import de.ad.sharp.api.SharedPreference;

@SharedPreference public interface LocalPersistentStorage {
  String getStringPreference();
  void setStringPreference(String preference);
}
