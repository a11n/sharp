/**
 * SharP wraps your SharedPreferences into a clean, type-safe Java interface. It uses annotation
 * processing to generate the boilerplate code for you.
 * <p>
 * Just define an interface and annotate it to be a {@literal @}{@link
 * de.ad.sharp.api.SharedPreference} or a {@literal @}{@link
 * de.ad.sharp.api.DefaultSharedPreference}:
 * <pre><code>
 *   {@literal @}SharedPreference
 *   interface LocalStorage{
 *     String getMyStringPreference();
 *     void setMyStringPreference(String value);
 *     int getMyIntPreference();
 *     void setMyIntPreference(int value);
 *   }
 * </code></pre>
 * Use {@link de.ad.sharp.api.SharP} to instantiate the auto-generated implementation:
 * <pre><code>
 *   LocalStorage storage = SharP.getInstance(context, LocalStorage.class);
 * </code></pre>
 *
 * In order to be lightweight and convenient SharP is designed according to the <a href="https://en.wikipedia.org/wiki/Convention_over_configuration">Convention over Configuration paradigm.</a>
 *
 * <h3>Conventions</h3>
 *
 * <h4>Interface</h4>
 * <ul>
 *  <li>The fully qualified interface name is used as unique filename to store your key-value-pairs</li>
 *  <li>The SharedPreferences are always stored in MODE_PRIVATE</li>
 *  <li>Only top-level interfaces are supported</li>
 * </ul>
 *
 * <h4>Properties</h4>
 * <ul>
 *  <li><a href="https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions">JavaBean naming conventions</a> are applied</li>
 *  <li>Getters are required to:</li>
 *  <ul>
 *    <li>start with get and</li>
 *    <li>have no parameters</li>
 *    <li>Example: {@code String getMyStringPreference();}</li>
 *  </ul>
 *  <li>Boolean getters are required to:</li>
 *  <ul>
 *    <li>start with is</li>
 *    <li>have no parameters and</li>
 *    <li>must return boolean</li>
 *    <li>Example: {@code boolean isMyBooleanPreference();}</li>
 *  </ul>
 *  <li>Setters are required:</li>
 *  <ul>
 *    <li>to start with set</li>
 *    <li>have exactly one parameter</li>
 *    <li>must return void</li>
 *    <li>Example: {@code void setMyStringPreference(String value);}</li>
 *  </ul>
 *  <li>Each getters needs a corresponding setter</li>
 *  <ul>
 *    <li>Example: {@code int getMyIntPreference()} requires declaration of {@code void setMyIntPreference(int value)}</li>
 *  </ul>
 * </ul>
 *
 * <h4>Supported types</h4>
 * <ul>
 *  <li>SharP supports all types, but there is a twofold distinction:</li>
 *  <ul>
 *    <li>Native types (int, long, float, boolean and String)</li>
 *    <li>Custom types (any non native type)</li>
 *  </ul>
 *  <li>While native types are natively supported by <a href="http://developer.android.com/reference/android/content/SharedPreferences.html">SharedPreferences</a>, custom types will be serialized/deserialized and treated as String</li>
 * </ul>
 *
 * <h4>Default values</h4>
 * <ul>
 *  <li>If a value has not been set yet, <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html">Java's default values</a> are returned according to its type</li>
 * </ul>
 *
 * <h4>Specialities</h4>
 * <ul>
 *  <li>The declaration of {@code void reset()} in your interface will result in a special implementation which calls {@code editor.clear().apply()}</li>
 * </ul>
 */
package de.ad.sharp.api;