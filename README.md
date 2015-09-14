#SharP [![Build Status](https://travis-ci.org/a11n/sharp.svg?branch=master)](https://travis-ci.org/a11n/sharp) [![Coverage Status](https://coveralls.io/repos/a11n/sharp/badge.svg?branch=master&service=github)](https://coveralls.io/github/a11n/sharp?branch=master) [ ![Download](https://api.bintray.com/packages/a11n/maven/de.ad%3Asharp-api/images/download.svg) ](https://bintray.com/a11n/maven/de.ad%3Asharp-api/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SharP-green.svg?style=flat)](https://android-arsenal.com/details/1/2483)

SharP wraps your **Shar**ed**P**references into a clean, type-safe Java interface. It uses annotation processing to generate the boilerplate code for you.

Just declare your interface and annotate it to be a `@SharedPreference` or a `@DefaultSharedPreference`:
```java
@SharedPreference
interface LocalStorage{
  String getMyStringPreference();
  void setMyStringPreference(String value);
  int getMyIntPreference();
  void setMyIntPreference(int value);
}
```

...then instantiate its auto-generated implementation using `SharP`:

```java
LocalStorage storage = SharP.getInstance(context, LocalStorage.class);

String myStringPreference = storage.getMyStringPreference();
int myIntPreference = storage.getMyIntPreference();

storage.setMyStringPreference("FooBar");
storage.setMyIntPreference(42);
```
That's it. No struggling with keys anymore.

Please refer to the [Javadoc](http://a11n.github.io/sharp/javadoc) or the [sample application](https://github.com/a11n/sharp/tree/master/sharp-sample) for more information.

##Usage
SharP is available via `jcenter()`. The [android-apt](https://bitbucket.org/hvisser/android-apt) plugin is used to setup SharP as annotation processor in Android Studio.
```java
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
  compile 'de.ad:sharp-api:0.3.0'
  apt 'de.ad:sharp-processor:0.3.0'
}
```

##Principles
In order to be lightweight and convenient SharP is designed according to the [Convention over Configuration paradigm](https://en.wikipedia.org/wiki/Convention_over_configuration).

###Conventions
####Interface
* `@DefaultSharedPreference`:
 * The default `SharedPreferences` are used calling `PreferencesManager.getDefaultSharedPreferences(Context context)`
 * Use this if you want to access the stored preferences from  [PreferenceFragment](http://developer.android.com/reference/android/preference/PreferenceFragment.html)
* `@SharedPreference`:
 * Preferences are stored in their own file in `MODE_PRIVATE`
 * The fully qualified `interface` name is used as unique filename
* Only top-level interfaces are supported

####Properties
* [JavaBean naming conventions](https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions) are applied
* Getters are required to:
 * start with `get` and
 * have no parameters
 * Example: `String getMyStringPreference();`
* Boolean getters are required to:
 * start with `is`
 * have no parameters and
 * must return `boolean`
 * Example: `boolean isMyBooleanPreference();`
* Setters are required:
 * to start with `set`
 * have exactly one parameter
 * must return `void`
 * Example: `void setMyStringPreference(String value);`
* Each getters needs a corresponding setter
 * Example: `int getMyIntPreference()` requires declaration of `void setMyIntPreference(int value)`

####Supported types
* SharP supports all types, but there is a twofold distinction:
 * Native types (`int`, `long`, `float`, `boolean` and `String`)
 * Custom types (any non native type)
* While *native types* are natively supported by [SharedPreferences](http://developer.android.com/reference/android/content/SharedPreferences.html), *custom types* will be serialized/deserialized and treated as `String`

####Default values
* If a value has not been set yet, [Java's default values](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) are returned according to its type

####Specialities
* The declaration of `void reset()` in your interface will result in a special implementation which calls `editor.clear().apply()`
