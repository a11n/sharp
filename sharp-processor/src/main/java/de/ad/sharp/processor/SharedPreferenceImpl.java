package de.ad.sharp.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class SharedPreferenceImpl {
  private static final List<TypeName> VALID_TYPES =
      Arrays.asList(TypeName.INT, TypeName.LONG, TypeName.FLOAT, TypeName.BOOLEAN,
          ClassName.bestGuess("java.lang.String"));

  private final TypeSpec typeSpec;
  private final Collection<FieldSpec> fields;

  public static SharedPreferenceImpl of(TypeElement annotatedType) {
    return new SharedPreferenceImpl(annotatedType);
  }

  private SharedPreferenceImpl(TypeElement annotatedInterface) {
    verifyArgumentIsInterface(annotatedInterface);

    String className = annotatedInterface.getSimpleName() + "Impl";
    ClassName interfaceName = ClassName.get(annotatedInterface);
    fields = new ArrayList<>();
    MethodSpec constructor = generateConstructor(className);
    Iterable<MethodSpec> methods = generateMethodsOf(annotatedInterface);

    typeSpec = TypeSpec.classBuilder(className)
        .addSuperinterface(interfaceName)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addFields(fields)
        .addMethod(constructor)
        .addMethods(methods)
        .build();
  }

  private MethodSpec generateConstructor(String className) {
    ClassName context = ClassName.bestGuess("android.content.Context");
    ClassName sharedPreferences = ClassName.bestGuess("android.content.SharedPreferences");
    ClassName editor = ClassName.bestGuess("android.content.SharedPreferences.Editor");

    FieldSpec sharedPreferencesField =
        FieldSpec.builder(sharedPreferences, "sharedPreferences", Modifier.PRIVATE, Modifier.FINAL)
            .build();
    FieldSpec editorField =
        FieldSpec.builder(editor, "editor", Modifier.PRIVATE, Modifier.FINAL).build();

    fields.add(sharedPreferencesField);
    fields.add(editorField);

    CodeBlock code = CodeBlock.builder()
        .addStatement("$L = $L.$L($S, $L)", "this.sharedPreferences", "context",
            "getSharedPreferences", className, "Context.MODE_PRIVATE")
        .addStatement("$L = $L", "this.editor", "this.sharedPreferences.edit()")
        .build();

    return MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(context, "context")
        .addCode(code)
        .build();
  }

  private Iterable<MethodSpec> generateMethodsOf(TypeElement annotatedInterface) {
    List<MethodSpec> methodSpecs = new ArrayList<>();

    Iterable<ExecutableElement> methods = getMethodsOf(annotatedInterface);
    for (ExecutableElement method : methods) {
      MethodSpec methodSpec = generateMethodOf(method);

      methodSpecs.add(methodSpec);
    }

    return methodSpecs;
  }

  private MethodSpec generateMethodOf(ExecutableElement method) {
    String name = method.getSimpleName().toString();
    List<ParameterSpec> parameters = generateParametersOf(method);
    TypeName returnType = TypeName.get(method.getReturnType());
    CodeBlock code = generateCode(name, parameters, returnType);

    return MethodSpec.methodBuilder(name)
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addParameters(parameters)
        .addCode(code)
        .returns(returnType)
        .build();
  }

  private CodeBlock generateCode(String name, List<ParameterSpec> parameters, TypeName returnType) {
    if (name.startsWith("get")) {
      return generateGetter(name, parameters, returnType);
    } else if (name.startsWith("set")) {
      return generateSetter(name, parameters, returnType);
    } else {
      throw new IllegalArgumentException(
          "Method names are supposed to either start with 'get' or 'set'.");
    }
  }

  private CodeBlock generateGetter(String name, List<ParameterSpec> parameters,
      TypeName returnType) {
    verifyGetterDeclaration(parameters, returnType);

    String getterName = computeGetterNameFor(returnType);
    String key = computeKeyForMethod(name);
    String defaultValue = getDefaultValueFor(returnType);

    return CodeBlock.builder()
        .addStatement("return $L.$L($S, $L)", "sharedPreferences", getterName, key, defaultValue)
        .build();
  }

  private String computeKeyForMethod(String name) {
    if (name.startsWith("get") || name.startsWith("set")) name = name.substring(3);

    return camelToUnderscore(name);
  }

  private String camelToUnderscore(String name) {
    StringBuilder builder = new StringBuilder(name.length() * 2);

    builder.append(Character.toLowerCase(name.charAt(0)));
    for (int i = 1; i < name.length(); i++) {
      char charAt = name.charAt(i);
      if (Character.isUpperCase(charAt)) builder.append('_');
      builder.append(Character.toLowerCase(charAt));
    }

    return builder.toString();
  }

  private String computeGetterNameFor(TypeName returnType) {
    String type = returnType.toString();
    //java.lang.String -> String
    type = type.contains(".") ? type.substring(type.lastIndexOf(".") + 1) : type;
    return "get" + capitalize(type);
  }

  private String capitalize(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  private String getDefaultValueFor(TypeName returnType) {
    switch (returnType.toString()) {
      case "java.lang.String":
        return "null";
      case "int":
        return "0";
      case "long":
        return "0";
      case "float":
        return "0.0f";
      case "boolean":
        return "false";
    }

    throw new IllegalArgumentException();
  }

  private void verifyGetterDeclaration(List<ParameterSpec> parameters, TypeName returnType) {
    if (parameters.size() > 0) {
      throw new IllegalArgumentException("Getters are not allowed to have parameters.");
    } else if (!isValid(returnType)) {
      throw new IllegalArgumentException("This is not a valid return type for getters.");
    }
  }

  private CodeBlock generateSetter(String name, List<ParameterSpec> parameters,
      TypeName returnType) {
    verifySetterDeclaration(parameters, returnType);

    String setterName = computeSetterNameFor(parameters.get(0).type);
    String key = computeKeyForMethod(name);
    String value = parameters.get(0).name;

    return CodeBlock.builder()
        .addStatement("$L.$L($S, $L).apply()", "editor", setterName, key, value)
        .build();
  }

  private String computeSetterNameFor(TypeName parameterType) {
    String type = parameterType.toString();
    //java.lang.String -> String
    type = type.contains(".") ? type.substring(type.lastIndexOf(".") + 1) : type;
    return "put" + capitalize(type);
  }

  private void verifySetterDeclaration(List<ParameterSpec> parameters, TypeName returnType) {
    if (parameters.size() != 1) {
      throw new IllegalArgumentException("Setters are supposed to have exactly one parameter.");
    } else if (!isValid(parameters.get(0).type)) {
      throw new IllegalArgumentException("This is not a valid parameter type for setters.");
    } else if (!TypeName.VOID.equals(returnType)) {
      throw new IllegalArgumentException("Setters are supposed to return void.");
    }
  }

  private boolean isValid(TypeName type) {
    return VALID_TYPES.contains(type);
  }

  private List<ParameterSpec> generateParametersOf(ExecutableElement method) {
    List<ParameterSpec> parameters = new ArrayList<>();

    List<? extends VariableElement> methodParameters = method.getParameters();
    for (VariableElement methodParameter : methodParameters) {
      TypeName type = TypeName.get(methodParameter.asType());
      String name = methodParameter.getSimpleName().toString();
      Set<Modifier> methodModifiers = methodParameter.getModifiers();
      Modifier[] modifiers = methodModifiers.toArray(new Modifier[methodModifiers.size()]);

      ParameterSpec parameter = ParameterSpec.builder(type, name, modifiers).build();

      parameters.add(parameter);
    }

    return parameters;
  }

  private Iterable<ExecutableElement> getMethodsOf(TypeElement annotatedInterface) {
    List<ExecutableElement> methods = new ArrayList<>();

    List<? extends Element> enclosedElements = annotatedInterface.getEnclosedElements();
    for (Element element : enclosedElements)
      if (element instanceof ExecutableElement) methods.add((ExecutableElement) element);

    return methods;
  }

  public JavaFile toJava(String packageName) {
    return JavaFile.builder(packageName, typeSpec)
        .addFileComment("Auto-generated by SharP. Please do not modify!")
        .build();
  }

  private void verifyArgumentIsInterface(TypeElement annotatedType) {
    if (annotatedType.getKind() != ElementKind.INTERFACE) {
      throw new IllegalArgumentException("@SharedPreference is only allowed for interfaces.");
    }
  }
}
