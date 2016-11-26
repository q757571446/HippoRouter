package com.example.module.compile;

import com.example.module.annotation.RouterMap;
import com.example.module.compile.exception.TargetErrorException;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor{
    private Messager mMessager;
    private Filer mFiler;
    private Elements elementUtils;
    private boolean writerRoundDone;

    public static final String SUFFIX = "_RouterBinding";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        mMessager.printMessage(Diagnostic.Kind.NOTE,
                "RouterProcessor start init..."+processingEnv.getOptions());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(RouterMap.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(RouterMap.class);

        try{
            if (env.processingOver()) {
                if (!annotations.isEmpty()) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR,
                            "Unexpected processing state: annotations still available after processing over");
                    return false;
                }
            }
            if (annotations.isEmpty()) {
                return false;
            }
            if (writerRoundDone) {
                mMessager.printMessage(Diagnostic.Kind.ERROR,
                        "Unexpected processing state: annotations still available after writing.");
            }

            if (!elements.isEmpty()) {
                mMessager.printMessage(Diagnostic.Kind.NOTE,"processing RouterMap class build...");
                generateRouterFactory(elements);
            } else {
                mMessager.printMessage(Diagnostic.Kind.WARNING, "No RouterMap annotations found");
            }

            writerRoundDone = true;
        } catch (IOException e) {
            // IntelliJ does not handle exceptions nicely, so log and print a message
            e.printStackTrace();
            mMessager.printMessage(Diagnostic.Kind.ERROR, "Unexpected error in RouterAnnotationProcessor: " + e);
        }
        return true;
    }


    private void generateRouterFactory(Set<? extends Element> elements) throws IOException {
        for (Element element : elements) {
            createRouterFactory("com.example.router","com.example.library.router.factory.RouterBinder", (TypeElement) element);
        }
    }

    private void createRouterFactory(String packageName, String superClazzPath, TypeElement element) throws IOException {
        if(element.getKind() != ElementKind.CLASS){
            throw new TargetErrorException();
        }
        ParameterizedTypeName mapTypeName = ParameterizedTypeName
                .get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(Class.class));
        ParameterizedTypeName hashMapTypeName = ParameterizedTypeName
                .get(ClassName.get(HashMap.class), ClassName.get(String.class), ClassName.get(Class.class));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getRouterTable")
                .addAnnotation(Override.class) //add ann
                .addModifiers(Modifier.PUBLIC)
                .returns(mapTypeName);

        RouterMap router = element.getAnnotation(RouterMap.class);//get annotation in activity
        String routerUrl = router.value();//get routerurl
        if(routerUrl != null){
            methodBuilder.addStatement("$T router = new $T()",mapTypeName,hashMapTypeName);
            methodBuilder.addStatement("router.put($S, $T.class)", routerUrl, ClassName.get(element));
            methodBuilder.addStatement("return router");
        }

        TypeElement routerInitializerType = elementUtils.getTypeElement(superClazzPath);
        String generateClassName = getGenerateClassName(routerUrl);
        TypeSpec clazz = TypeSpec.classBuilder(generateClassName)
                .addSuperinterface(ClassName.get(routerInitializerType))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, clazz).build();
        javaFile.writeTo(mFiler);
    }

//    activity://simple/:s{name}
    private String getGenerateClassName(String url) {
        String scheme = getScheme(url);

        String host = getHost(url);
        return new StringBuffer()
                .append(toUpperCaseFirstOne(scheme))
                .append(toUpperCaseFirstOne(host))
                .append(SUFFIX)
                .toString();
    }

    private String getScheme(String url) {
        return url.substring(0, url.indexOf(":"));
    }



    private String getHost(String url) {
        String replace = url.replace("//", "");
        mMessager.printMessage(Diagnostic.Kind.NOTE,"generate router bind class>>>"+replace);
        if (replace.indexOf("/") != -1) {
            return replace.substring(replace.indexOf(":") + 1, replace.indexOf("/"));
        } else if (replace.indexOf("#") != -1) {
            return replace.substring(replace.indexOf(":") + 1,replace.indexOf("#"));
        } else {
            return replace.substring(replace.indexOf(":") + 1);
        }
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
