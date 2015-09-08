package de.ad.sharp.processor;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class SharedPreferenceProcessorTest {
  @Test public void shouldCompileWithoutError() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   int getIntPreference();",
            "   void setIntPreference(int value);", "   long getLongPreference();",
            "   void setLongPreference(long value);", "   float getFloatPreference();",
            "   void setFloatPreference(float value);", "   boolean isBooleanPreference();",
            "   void setBooleanPreference(boolean value);", "   String getStringPreference();",
            "   void setStringPreference(String value);", "void reset();", "}"));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("LocalStorageImpl",
        Joiner.on('\n')
            .join("package test;", "import de.ad.sharp.api.SharedPreference;",
                "@SharedPreference public interface LocalStorage {", "   int getIntPreference();",
                "   void setIntPreference(int value);", "   long getLongPreference();",
                "   void setLongPreference(long value);", "   float getFloatPreference();",
                "   void setFloatPreference(float value);", "   boolean getBooleanPreference();",
                "   void setBooleanPreference(boolean value);", "   String getStringPreference();",
                "   void setStringPreference(String value);", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .compilesWithoutError();
    //http://stackoverflow.com/questions/32394726/assertionerror-when-unit-testing-an-annotation-processor-with-compile-testing
    //.and().generatesSources(expectedSource);
  }

  @Test public void shouldFailWithIllegalTypeError() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public abstract class LocalStorage {",
            "   public void getIntPreference();", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_TYPE);
  }

  @Test public void shouldFailWithIllegalMessageName() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   int intPreference();", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_MESSAGE_NAME);
  }

  @Test public void shouldFailWithIllegalGetterParameterCount() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {",
            "   int getIntPreference(int value);", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_GETTER_PARAMETER_COUNT);
  }

  @Test public void shouldFailWithIllegalGetterReturnType() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   void getIntPreference();",
            "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_GETTER_RETURN_TYPE);
  }

  @Test public void shouldFailWithIllegalBooleanGetterMessageName() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   boolean getIntPreference();",
            "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_BOOLEAN_GETTER_MESSAGE_NAME);
  }

  @Test public void shouldFailWithIllegalBooleanGetterReturnType() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   int isIntPreference();", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_BOOLEAN_GETTER_RETURN_TYPE);
  }

  @Test public void shouldFailWithIllegalSetterParameterCount() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   void setIntPreference();",
            "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_SETTER_PARAMETER_COUNT);
  }

  @Test public void shouldFailWithIllegalSetterParameterType() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {",
            "   void setIntPreference(char value);", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_SETTER_PARAMETER_TYPE);
  }

  @Test public void shouldFailWithIllegalSetterReturnType() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {",
            "   int setIntPreference(int value);", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_SETTER_RETURN_TYPE);
  }

  @Test public void shouldFailWithIllegalResetParameterCount() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   void reset(int value);", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_RESET_PARAMETER_COUNT);
  }

  @Test public void shouldFailWithIllegalResetReturnType() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   boolean reset();", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(SharedPreferenceImpl.ILLEGAL_RESET_RETURN_TYPE);
  }

  @Test public void shouldFailWithIllegalNoSetterForGetter() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {", "   int getIntPreference();",
            "}"));

    String expectedError =
        String.format(SharedPreferenceImpl.ILLEGAL_NO_SETTER_FOR_GETTER, "getIntPreference");

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(expectedError);
  }

  @Test public void shouldFailWithIllegalNoGetterForSetter() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("LocalStorage", Joiner.on('\n')
        .join("package test;", "import de.ad.sharp.api.SharedPreference;",
            "@SharedPreference public interface LocalStorage {",
            "   void setIntPreference(int value);", "}"));

    String expectedError =
        String.format(SharedPreferenceImpl.ILLEGAL_NO_GETTER_FOR_SETTER, "setIntPreference");

    assertAbout(javaSource()).that(source)
        .processedWith(new SharedPreferenceProcessor())
        .failsToCompile()
        .withErrorContaining(expectedError);
  }
}
