package com.example.library.compile;

import com.example.library.annotation.RouterMap;
import com.example.library.compile.exception.TargetErrorException;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.Collections;
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

import static com.example.library.annotation.uri.RouterUtils.getGenerateClassName;
import static com.example.library.annotation.uri.RouterUtils.getGeneratePackageName;


@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor{
    private Messager mMessager;
    private Filer mFiler;
    private Elements elementUtils;
    private boolean writerRoundDone;


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
                generateInitializer(elements);
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


    private void generateInitializer(Set<? extends Element> elements) throws IOException {
        for (Element element : elements) {
            writeInitializer((TypeElement) element);
        }
    }

    private void writeInitializer(TypeElement element) throws IOException {
        if(element.getKind() != ElementKind.CLASS){
            throw new TargetErrorException();
        }

        TypeElement initializer = elementUtils.getTypeElement("com.example.library.router.factory.RouterInitializer");
        ParameterizedTypeName superInterface = ParameterizedTypeName.get(ClassName.get(initializer), ClassName.OBJECT);

        ParameterizedTypeName mapTypeName = ParameterizedTypeName
                .get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)));

        ParameterSpec tables = ParameterSpec.builder(mapTypeName, "tables")
                .build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("initialize")
                .addAnnotation(Override.class) //add ann
                .addModifiers(Modifier.PUBLIC)
                .addParameter(tables);

        RouterMap router = element.getAnnotation(RouterMap.class);//get annotation in activity
        String routerUrl = router.value();//get routerurl
        if(routerUrl != null){
            methodBuilder.addStatement("tables.put($S, $T.class)", routerUrl, ClassName.get(element));
        }

        String generateClassName = getGenerateClassName(routerUrl);
        TypeSpec clazz = TypeSpec.classBuilder(generateClassName)
                .addSuperinterface(superInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(getGeneratePackageName(routerUrl), clazz).build();
        javaFile.writeTo(mFiler);
    }

}
