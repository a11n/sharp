#SHARP

An annotation based **Shar**ed**P**references wrapper.

**!!! This library is still under development. !!!**

##Motivation
Android features [a couple of data storage options](http://developer.android.com/guide/topics/data/data-storage.html). One of them is [SharedPreferences](http://developer.android.com/guide/topics/data/data-storage.html#pref):
>The SharedPreferences class provides a general framework that allows you to save and retrieve persistent key-value pairs of primitive data types.

When using `SharedPreferences` you will most commonly end up to manage **a variety of keys** on your own and write **a lot of code which looks like this:**

```java
SharedPreferences sharedPreferences = context.getSharedPreferences("my_prefs", MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPreferences.edit();

String myStringPreference = sharedPreferences.getString("my_string_preferences", null);
int myIntPreference = sharedPreferences.getInt("my_int_preferences", 0);
...

editor.putString("my_string_preference", "FooBar");
editor.putInt("my_int_preference", 42);
...

```

SharP aims to solve this problem. It uses [annotation processing](http://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/Processor.html) to generate the boilerplate code for you. The only thing you need to specify is a clean `interface` and annotate it to be a `@SharedPreference`:

```java
@SharedPreference
interface LocalStorage{
  String getMyStringPreference();
  void setMyStringPreference(String value);
  int getMyIntPreference();
  void setMyIntPreference(int value);
}
```

Wherever you need to access your persistent data just grab an instance of your `LocalStorage` using SharP (ideally you do this once in your `Application` class):

```java
LocalStorage storage = SharP.in(context).load(LocalStorage.class);
String myStringPreference = storage.getMyStringPreference();
```

##Principles
In order to be lightweight and convenient SharP is designed according to the [Convetion over Configuration paradigm](https://en.wikipedia.org/wiki/Convention_over_configuration).

###Conventions
* The `interface` name is used as filename to store your key-value-pairs
* Getters are required to:
 * start with `get`
 * have no parameters and
 * must return a *valid type*
 * Example: `String getMyStringPreference();`
* Setters are required:
 * to start with `set`
 * have exactly one parameter of a *valid type*
 * must return `void`
 * Example: `void setMyStringPreference(String value);`
* the method name is formatted to underscore and used as key
  * Example: `getMyStringPreference() -> key: my_string_preference`
* Valid types are: `int`, `long`, `float`, `boolean` and `String`
