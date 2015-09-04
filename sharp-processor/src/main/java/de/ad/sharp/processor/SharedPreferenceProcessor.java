package de.ad.sharp.processor;

import com.squareup.javapoet.JavaFile;
import de.ad.sharp.api.SharedPreference;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class SharedPreferenceProcessor extends AbstractProcessor {
  private Messager messager;
  private Elements elements;

  @Override public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(SharedPreference.class.getCanonicalName());
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    messager = processingEnv.getMessager();
    elements = processingEnv.getElementUtils();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SharedPreference.class))
      process((TypeElement) annotatedElement);

    return true;
  }

  private void process(TypeElement annotatedType) {
    String packageName = getPackageNameOf(annotatedType);
    JavaFile javaFile = SharedPreferenceImpl.of(annotatedType).toJava(packageName);

    tryToWrite(javaFile);
  }

  private String getPackageNameOf(Element element) {
    return elements.getPackageOf(element).toString();
  }

  private void tryToWrite(JavaFile javaFile) {
    try {
      javaFile.writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
