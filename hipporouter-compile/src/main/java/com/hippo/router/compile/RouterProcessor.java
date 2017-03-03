package com.hippo.router.compile;

import com.hippo.router.compile.exception.TargetErrorException;
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

import static com.hippo.router.compile.utils.RouterUtils.GENERATE_CLASS_NAME;
import static com.hippo.router.compile.utils.RouterUtils.GENERATE_PACKAGE_NAME;

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
        return Collections.singleton(Route.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(Route.class);

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
                mMessager.printMessage(Diagnostic.Kind.NOTE,"processing Route class build...");
                generateInitializer(elements);
            } else {
                mMessager.printMessage(Diagnostic.Kind.WARNING, "No Route annotations found");
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
        TypeElement initializer = elementUtils.getTypeElement("com.hippo.router.factory.RouterInitializer");
        ParameterizedTypeName superInterface = ParameterizedTypeName.get(ClassName.get(initializer), ClassName.OBJECT);

        ParameterizedTypeName mapTypeName = ParameterizedTypeName
                .get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Object.class)));

        ParameterSpec tables = ParameterSpec.builder(mapTypeName, "tables")
                .build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("initialize")
                .addAnnotation(Override.class) //add ann
                .addModifiers(Modifier.PUBLIC)
                .addParameter(tables);

        for(Element element : elements){
            if(element.getKind() != ElementKind.CLASS){
                throw new TargetErrorException();
            }
            String router = element.getAnnotation(Route.class).value();//get annotation in activity

            ClassName clazz = ClassName.get((TypeElement)element);
            methodBuilder.addStatement("tables.put($S, $T.class)", router, clazz);
        }


        TypeSpec clazz = TypeSpec.classBuilder(GENERATE_CLASS_NAME)
                .addSuperinterface(superInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(GENERATE_PACKAGE_NAME, clazz).build();
        javaFile.writeTo(mFiler);

    }




}
