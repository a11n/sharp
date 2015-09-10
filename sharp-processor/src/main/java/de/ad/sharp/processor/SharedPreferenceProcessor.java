package de.ad.sharp.processor;

import de.ad.sharp.api.DefaultSharedPreference;
import de.ad.sharp.api.SharedPreference;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static javax.tools.Diagnostic.Kind.ERROR;

public class SharedPreferenceProcessor extends AbstractProcessor {
  private Messager messager;
  private Filer filer;

  @Override public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportedAnnotationTypes = new HashSet<>(2);

    supportedAnnotationTypes.add(DefaultSharedPreference.class.getCanonicalName());
    supportedAnnotationTypes.add(SharedPreference.class.getCanonicalName());

    return supportedAnnotationTypes;
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    messager = processingEnv.getMessager();
    filer = processingEnv.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      processAnnotations(roundEnv);
    } catch (Exception e) {
      error(e.getMessage());
    }

    return true;
  }

  private void processAnnotations(RoundEnvironment roundEnv) throws IOException {
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(
        DefaultSharedPreference.class))
      processDefaultSharedPreference((TypeElement) annotatedElement);

    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SharedPreference.class))
      processSharedPreference((TypeElement) annotatedElement);
  }

  private void processDefaultSharedPreference(TypeElement annotatedType) throws IOException {
    DefaultSharedPreferenceImpl.of(annotatedType).writeJavaTo(filer);
  }

  private void processSharedPreference(TypeElement annotatedType) throws IOException {
    SharedPreferenceImpl.of(annotatedType).writeJavaTo(filer);
  }

  private void error(String message) {
    messager.printMessage(ERROR, message);
  }
}
