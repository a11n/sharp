package de.ad.sharp.api;

import android.content.Context;
import java.lang.reflect.InvocationTargetException;

public class SharP {
  public static <T> T load(Context context, Class<T> clazz) {
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
