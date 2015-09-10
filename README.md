#SharP [![Build Status](https://travis-ci.org/a11n/sharp.svg?branch=master)](https://travis-ci.org/a11n/sharp) [![Coverage Status](https://coveralls.io/repos/a11n/sharp/badge.svg?branch=master&service=github)](https://coveralls.io/github/a11n/sharp?branch=master) [ ![Download](https://api.bintray.com/packages/a11n/maven/de.ad%3Asharp-api/images/download.svg) ](https://bintray.com/a11n/maven/de.ad%3Asharp-api/_latestVersion)

SharP wraps your **Shar**ed**P**references into a clean, type-safe Java interface. It uses annotation processing to generate the boilerplate code for you.

Just declare your interface and annotate it to be a `@SharedPreference`:
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
  compile 'de.ad:sharp-api:0.1.0'
  apt 'de.ad:sharp-processor:0.1.0'
}
```

##Code Generation
SharP uses annotation processing and a couple of conventions to generate the boilerplate code for you.

This is what you need to specify:

```java
package my.package;

@SharedPreference
interface LocalStorage{
  String getMyStringPreference();
  void setMyStringPreference(String value);
  int getMyIntPreference();
  void setMyIntPreference(int value);
}
```

...and this is what SharP generates for you:

```java
public final class LocalStorageImpl implements LocalStorage {
  private final SharedPreferences sharedPreferences;
  private final SharedPreferences.Editor editor;

  public LocalStorageImpl(Context context) {
    this.sharedPreferences =
      context.getSharedPreferences("my.package.LocalStorage", Context.MODE_PRIVATE);
    this.editor = this.sharedPreferences.edit();
  }

  @Override
  public final String getMyStringPreference() {
    return sharedPreferences.getString("my_string_preference", null);
  }

  @Override
  public final void setMyStringPreference(String value) {
    editor.putString("my_string_preference", value).apply();
  }

  @Override
  public final int getMyIntPreference() {
    return sharedPreferences.getInt("my_int_preference", 0);
  }

  @Override
  public final void setMyIntPreference(int value) {
    editor.putInt("my_int_preference", value).apply();
  }
}
```

##Principles
In order to be lightweight and convenient SharP is designed according to the [Convention over Configuration paradigm](https://en.wikipedia.org/wiki/Convention_over_configuration).

###Conventions
####Interface
* The fully qualified `interface` name is used as unique filename to store your key-value-pairs
* The `SharedPreferences` are always stored in `MODE_PRIVATE`
* Only top-level interfaces are supported

####Properties
* [JavaBean naming conventions](https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions) are applied
* Getters are required to:
 * start with `get`
 * have no parameters and
 * must return a *valid type*
 * Example: `String getMyStringPreference();`
* Boolean getters are required to:
 * start with `is`
 * have no parameters and
 * must return `boolean`
 * Example: `boolean isMyBooleanPreference();`
* Setters are required:
 * to start with `set`
 * have exactly one parameter of a *valid type*
 * must return `void`
 * Example: `void setMyStringPreference(String value);`
* Each getters needs a corresponding setter
 * Example: `int getMyIntPreference()` requires declaration of `void setMyIntPreference(int value)`

####Valid types
* Valid types are: `int`, `long`, `float`, `boolean` and `String`

####Default values
* If a value has not been set yet, [Java's default values](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) are returned according to its type

####Specialities
* The declaration of `void reset()` in your interface will result in a special implementation which calls `editor.clear().apply()`
