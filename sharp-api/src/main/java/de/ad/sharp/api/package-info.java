/**
 * SharP wraps your SharedPreferences into a clean, type-safe Java interface. It uses annotation
 * processing to generate the boilerplate code for you.
 * <p>
 * Just define an interface and annotate it to be a {@literal @}{@link
 * de.ad.sharp.api.SharedPreference}:
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
 */
package de.ad.sharp.api;