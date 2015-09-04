package de.ad.sharp.processor;

import com.squareup.javapoet.JavaFile;
import de.ad.sharp.api.SharedPreference;
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

import static javax.tools.Diagnostic.Kind.ERROR;

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

    try {
      JavaFile javaFile = SharedPreferenceImpl.of(annotatedType).toJavaIn(packageName);
      javaFile.writeTo(processingEnv.getFiler());
    } catch(Exception e){
      error(e.getMessage());
    }
  }

  private String getPackageNameOf(Element element) {
    return elements.getPackageOf(element).toString();
  }

  private void error(String message) {
    messager.printMessage(ERROR, message);
  }
}
