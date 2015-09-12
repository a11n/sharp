#SharP sample application

This projects showcases how to use SharP.

![Screenshot 1](screenshot1.png)![Screenshot 2](screenshot2.png)

##Project setup
SharP uses annotation processing to generate the boilerplate code for you. Thus you need to setup SharP as annotation processor first. Therefore the [android-apt](https://bitbucket.org/hvisser/android-apt) plugin is used to setup SharP as annotation processor in Android Studio. This is done by **adding** the following lines to your `build.gradle`:

```groovy
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.6'
  }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {
  compile 'de.ad:sharp-api:0.1.0'
  apt 'de.ad:sharp-processor:0.1.0'
}
```

##Usage
The project setup already was the complex part, because using SharP is quite simple.

In the sample a [PreferenceFragment](http://developer.android.com/reference/android/preference/PreferenceFragment.html) is used to maintain some application settings. We are accessing these settings from other areas of the application by using SharP's `@DefaultSharedPreference` annotation. Therefore we declare the following interface:

```java
@DefaultSharedPreference
public interface Settings {
  int getMyIntPreference();
  void setMyIntPreference(int value);

  float getMyFloatPreference();
  void setMyFloatPreference(float value);

  boolean isMyBooleanPreference();
  void setMyBooleanPreference(boolean value);
  
  String getMyStringPreference();
  void setMyStringPreference(String value);
}
```

The `@DefaultSharedPreference` annotation generates code which loads a `SharedPreferences` instance calling `PreferencesManager.getDefaultSharedPreferences(context)`. This is the same place where `PreferenceFragments` stores its preferences. But actually we do not need to take of details like this.

In order to obtain an instance of our `Settings` we finally need to call:

```java
Settings settings = SharP.getInstance(this, Settings.class);
```
