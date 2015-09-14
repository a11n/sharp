package de.ad.sharp.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotates an interface to auto-generate its implementation using Sharp's annotation processor.
 * <p>
 * For this interface declaration:
 * <pre><code>
 *   {@literal @}SharedPreference
 *   interface LocalStorage{
 *     String getMyStringPreference();
 *     void setMyStringPreference(String value);
 *   }
 * </code></pre>
 * ...the following code will be auto-generated:
 * <pre><code>
 *   public final class LocalStorageImpl implements LocalStorage {
 *     private final SharedPreferences sharedPreferences;
 *     private final SharedPreferences.Editor editor;
 *     private final Gson gson;
 *
 *     public LocalStorageImpl(Context context) {
 *       this.sharedPreferences =
 *       context.getSharedPreferences("my.package.LocalStorage", Context.MODE_PRIVATE);
 *       this.editor = this.sharedPreferences.edit();
 *       this.gson = new Gson();
 *     }
 *
 *     {@literal @}Override
 *     public final String getMyStringPreference() {
 *        return sharedPreferences.getString("my_string_preference", null);
 *     }
 *
 *     {@literal @}Override
 *     public final void setMyStringPreference(String value) {
 *       editor.putString("my_string_preference", value).apply();
 *     }
 *   }
 * </code></pre>
 */
@Target(ElementType.TYPE)
public @interface SharedPreference {
}
