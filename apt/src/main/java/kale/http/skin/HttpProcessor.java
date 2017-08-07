package kale.http.skin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import kale.http.skin.annotation.ApiInterface;
import kale.http.skin.annotation.HttpGet;
import kale.http.skin.annotation.HttpPost;

/**
 * @author Jack Tony
 * @date 2015/8/16
 */
public class HttpProcessor extends AbstractProcessor {

    private static final String TAG = "[HttpProcessor]";

    public static final String PACKAGE_NAME = "kale.http.skin";

    public static final String CLASS_NAME = "HttpRequestEntity";

    private static final String PARENT_CLASS_NAME = NullInterface.class.getName();

    StringBuilder sb;

    private Elements elementUtils;

    public HttpProcessor() {
        // use {{ to replace { in Phrase
        String classBlockStr = Phrase.from("package {pkg_name};\n"
                + "import {http_request_whole_name};\n"
                + "import java.util.Map;\n"
                + "import com.google.gson.reflect.TypeToken;\n"
                + "import android.support.v4.util.ArrayMap;\n"
                + "public class {class_name} implements {parent_class} {{\n"
                + "    private {http_request} mHttpRequest;\n"
                + "    public HttpRequestEntity({http_request} httpRequest) {{\n"
                + "        mHttpRequest = httpRequest;\n"
                + "    }\n")
                .put("pkg_name", PACKAGE_NAME)
                .put("http_request_whole_name", HttpRequest.class.getName())
                .put("class_name", CLASS_NAME)
                .put("parent_class", PARENT_CLASS_NAME)
                .put("http_request", HttpRequest.class.getSimpleName())
                .format();

        sb = new StringBuilder(classBlockStr);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement te : annotations) {
            for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
                if (e.getKind() == ElementKind.INTERFACE) {
                    TypeElement ele = (TypeElement) e;
                    if (ele.getAnnotation(ApiInterface.class) != null) {
                        String interFaceName = ele.getQualifiedName().toString();
                        sb = createClzMainBlock(interFaceName, sb);
                    } else {
                        fatalError("Should use " + ApiInterface.class.getName());
                    }
                } else if (e.getKind() == ElementKind.METHOD) {
                    ExecutableElement method = (ExecutableElement) e;
                    if (method.getAnnotation(HttpPost.class) != null) {
                        handlerHttp(sb, e, method, true);
                    } else {
                        handlerHttp(sb, e, method, false);
                    }
                }
            }
        }
        sb.append("}");
        createClassFile(PACKAGE_NAME, CLASS_NAME, sb.toString());
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(
                HttpGet.class.getCanonicalName(),
                HttpPost.class.getCanonicalName(),
                ApiInterface.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * add new interface to class
     */
    StringBuilder createClzMainBlock(String interFaceName, StringBuilder sb) {
        int start = sb.indexOf(PARENT_CLASS_NAME);
        return sb.insert(start, interFaceName + ", ");
    }

    private void handlerHttp(StringBuilder sb, Element ele, ExecutableElement method, boolean isPost) {
        log("Working on method: " + method.getSimpleName());

        String url;
        if (isPost) {
            url = ele.getAnnotation(HttpPost.class).value();
        } else {
            url = ele.getAnnotation(HttpGet.class).value();
        }
        if (url == null || url.isEmpty()) {
            fatalError("Url is empty");
            return;
        }
        String methodName = method.getSimpleName().toString();
        String methodType = method.getReturnType().toString();

        String methodParamsStr = getParamsString(method);

        String modelName = getModelName(url, methodType);
        Map<String, String> customParams = getCustomParams(method);
        Map<String, String> paramsFromUrl = UrlUtil.getParams(url);
        url = UrlUtil.getRealUrl(url);

        if (isPost) {
            sb.append(CodeGenerator.createPostSnippet(methodType, methodName, methodParamsStr,
                    url, customParams, paramsFromUrl, modelName));
        } else {
            sb.append(CodeGenerator.createGetSnippet(methodType, methodName, methodParamsStr,
                    url, customParams, paramsFromUrl, modelName));
        }
    }

    /**
     * Observable<Activity> -> Activity
     * or
     * Observable<List<String>> -> new TypeToken<List<String>>(){}.getType()
     */
    String getModelName(String url, String modelName) {
        if (modelName.contains("<")) {
            modelName = modelName.substring(modelName.indexOf("<") + 1, modelName.lastIndexOf(">"));
            if (modelName.contains("<")) {
                modelName = "new TypeToken<" + modelName + ">(){}.getType()";
            } else {
                modelName = getModelNameByUrl(url, modelName);
                modelName = !modelName.isEmpty() ? modelName + ".class" : "null";
            }
            return modelName;
        } else {
            return "null";
        }
    }

    private String getModelNameByUrl(String url, String modelName) {
        if (!modelName.contains(".")) { // com.kale.Model
            // Note:for jsonAnnotation lib
            // if this model without a package name,
            // it means the model is generate by system.We must give a package name for it.
            modelName = UrlUtil.url2packageName(url) + "." + modelName;
        }
        return modelName;
    }

    private String getParamsString(ExecutableElement method) {
        StringBuilder sb = new StringBuilder();

        List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.size() == 0) {
            return sb.toString();
        }
        String paramTypeString = method.toString();
        paramTypeString = paramTypeString.substring(
                paramTypeString.indexOf("(") + 1, paramTypeString.lastIndexOf(")"));

        String[] split = paramTypeString.split(",");
        for (int i = 0; i < parameters.size(); i++) {
            String type = split[i];
            String paramName = parameters.get(i).getSimpleName().toString();
            sb.append(type).append(" ").append(paramName).append(" ,");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    private Map<String, String> getCustomParams(ExecutableElement method) {
        Map<String, String> customParams = new LinkedHashMap<>();
        for (VariableElement parameter : method.getParameters()) {
            String paramName = parameter.getSimpleName().toString();
            customParams.put(paramName, paramName);
        }
        return customParams;
    }

    private void createClassFile(String PACKAGE_NAME, String clsName, String content) {
        //PackageElement pkgElement = elementUtils.getPackageElement("");
        TypeElement pkgElement = elementUtils.getTypeElement(PACKAGE_NAME);

        OutputStreamWriter osw = null;
        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(PACKAGE_NAME + "." + clsName, pkgElement);
            OutputStream os = fileObject.openOutputStream();
            osw = new OutputStreamWriter(os, Charset.forName("UTF-8"));
            osw.write(content, 0, content.length());

        } catch (IOException e) {
            //e.printStackTrace();
            //fatalError(e.getMessage());
        } finally {
            try {
                if (osw != null) {
                    osw.flush();
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                fatalError(e.getMessage());
            }
        }
    }

    private void log(String msg) {
        if (processingEnv.getOptions().containsKey("debug")) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, TAG + msg);
        }
    }

    private void fatalError(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, TAG + msg);
    }

}
