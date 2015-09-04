package de.ad.sharp.processor;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class SharedPreferenceProcessorTest {
  @Test public void testName() throws Exception {
    assert_().about(javaSource())
        .that(JavaFileObjects.forSourceString("LocalStorage",
            Joiner.on("\n").join(
                "package test;",
                "import de.ad.sharp.api.SharedPreference;",
                "@SharedPreference public interface LocalStorage {",
                "   String getStringPreference();",
                "}"
            )))
        .processedWith(new SharedPreferenceProcessor())
        .compilesWithoutError();
  }
}
