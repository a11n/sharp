package de.ad.sharp.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import javax.lang.model.element.TypeElement;

class DefaultSharedPreferenceImpl extends SharedPreferenceImpl {
  public static DefaultSharedPreferenceImpl of(TypeElement annotatedType) {
    return new DefaultSharedPreferenceImpl(annotatedType);
  }

  private DefaultSharedPreferenceImpl(TypeElement annotatedInterface) {
    super(annotatedInterface);
  }

  @Override protected CodeBlock generateConstructorCode(String fullyQualifiedName) {
    ClassName preferenceManager = ClassName.bestGuess("android.preference.PreferenceManager");

    return CodeBlock.builder()
        .addStatement("$L = $T.$L($L)", "this.sharedPreferences", preferenceManager,
            "getDefaultSharedPreferences", "context")
        .addStatement("$L = $L", "this.editor", "this.sharedPreferences.edit()")
        .addStatement("$L = $L", "this.gson", "new Gson()")
        .build();
  }
}
