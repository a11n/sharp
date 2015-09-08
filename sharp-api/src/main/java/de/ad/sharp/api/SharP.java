package de.ad.sharp.api;

import android.content.Context;
import java.lang.reflect.InvocationTargetException;

/**
 * Use this class to instantiate the auto-generated implementation:
 *
 * <pre><code>
 *   LocalStorage storage = SharP.getInstance(context, LocalStorage.class);
 * </code></pre>
 */
public final class SharP {

  private SharP(){}

  /**
   * Instantiates the auto-generated implementation of an interface annotated with {@literal @}{@link
   * SharedPreference}.
   *
   * @param context the {@link android.content.Context} used to load and store the {@link
  android.content.SharedPreferences}
   * @param clazz the interface which auto-generated implementation should be instantiated
   * @param <T> the type of the interface which auto-generated implementation should be
   * instantiated
   * @return returns an instance of the auto-generated implementation of the given interface
   * @throws IllegalArgumentException if the auto-generated implementation of the given interface
   * could not be instantiated
   */
  public static <T> T getInstance(Context context, Class<T> clazz) {
    try {
      Class<T> implementation = (Class<T>) Class.forName(clazz.getName() + "Impl");

      return implementation.getDeclaredConstructor(Context.class).newInstance(context);
    } catch (ClassNotFoundException
        | InvocationTargetException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException e) {
      e.printStackTrace();
    }

    throw new IllegalArgumentException();
  }
}
