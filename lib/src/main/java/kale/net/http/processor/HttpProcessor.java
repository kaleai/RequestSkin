package kale.net.http.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
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

import kale.net.http.annotation.ApiInterface;
import kale.net.http.annotation.HttpGet;
import kale.net.http.annotation.HttpPost;
import kale.net.http.impl.HttpRequest;
import kale.net.http.util.AnnotationSupportUtil;
import kale.net.http.util.HttpCodeSnippetUtil;
import kale.net.http.util.UrlUtil;


/**
 * @author Jack Tony
 * @date 2015/8/16
 */
public class HttpProcessor extends AbstractProcessor {

    private static final String TAG = "[ " + HttpProcessor.class.getSimpleName() + " ]:";

    public static final String PACKAGE_NAME = "kale.net.http";

    public static final String CLASS_NAME = "HttpRequestEntity";

    private String PARENT_CLASS_NAME = "java.io.Serializable";

    public StringBuilder getStringBuilder() {
        return mStringBuilder;
    }

    private StringBuilder mStringBuilder;

    public HttpProcessor() {
        String classBlockStr = "package " + PACKAGE_NAME + ";\n"
                + "\n"
                + "import " + HttpRequest.class.getName() + ";\n"
                + "import java.util.HashMap;\n"
                + "import rx.Observable;\n"
                + "\n"
                + "public class " + CLASS_NAME + " implements " + PARENT_CLASS_NAME + " {\n"
                + "\n"
                + "    private " + HttpRequest.class.getSimpleName() + " mHttpRequest;\n"
                + "\n"
                + "    public " + CLASS_NAME + "(" + HttpRequest.class.getSimpleName() + " httpRequest) {\n"
                + "        mHttpRequest = httpRequest;\n"
                + "    }\n"
                + "\n"
                + "\n";
        mStringBuilder = new StringBuilder(classBlockStr);
    }

    private Elements elementUtils;

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
                        mStringBuilder = createClsBlock(interFaceName, mStringBuilder);
                    } else {
                        fatalError("Should use " + ApiInterface.class.getName());
                    }
                } else if (e.getKind() == ElementKind.METHOD) {
                    ExecutableElement method = (ExecutableElement) e;
                    if (method.getAnnotation(HttpPost.class) != null) {
                        handlerHttp(mStringBuilder, e, method, true);
                    } else {
                        handlerHttp(mStringBuilder, e, method, false);
                    }
                }
            }
        }
        mStringBuilder.append("\n}");
        createClassFile(PACKAGE_NAME, CLASS_NAME, mStringBuilder.toString());
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new AnnotationSupportUtil()
                .support(HttpGet.class)
                .support(HttpPost.class)
                .support(ApiInterface.class).get();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public StringBuilder createClsBlock(String interFaceName, StringBuilder sb) {
        int start = sb.indexOf(PARENT_CLASS_NAME);
        return sb.insert(start, interFaceName + ", ");
    }

    public void handlerHttp(StringBuilder sb, Element ele, ExecutableElement method, boolean isPost) {
        log("Working on method: " + method.getSimpleName());

        String url;
        if (isPost) {
            url = ele.getAnnotation(HttpPost.class).value();
        } else {
            url = ele.getAnnotation(HttpGet.class).value();
        }
        if (url.equals("")) {
            fatalError("Url is empty");
            return;
        }
        String methodName = method.getSimpleName().toString();
        String modelName = getModelName(url, method.getReturnType().toString());
        Map<String, String> customParams = getCustomParams(method);
        Map<String, String> defaultParams = UrlUtil.getParams(url);
        url = UrlUtil.getRealUrl(url);
        
        if (isPost) {
            sb.append(HttpCodeSnippetUtil.createPostSnippet(methodName, url, customParams, defaultParams, modelName));
        } else {
            sb.append(HttpCodeSnippetUtil.createGetSnippet(methodName, url, customParams, defaultParams, modelName));
        }

        log("Parse method: " + method.getSimpleName() + " completed");
    }

    private String getModelName(String url, String modelName) {
        if (modelName.contains("<")) {
            modelName = modelName.substring(modelName.indexOf("<") + 1, modelName.lastIndexOf(">"));
        }
        if (!modelName.contains(".")) {
            // if this model without a packageName,
            // it means the model is generate by system.We must give a package name for it.
            modelName = UrlUtil.url2packageName(url) + "." + modelName;
        }
        return modelName;
    }

    private Map<String, String> getCustomParams(ExecutableElement method) {
        Map<String, String> customParams = new LinkedHashMap<>();
        List<? extends VariableElement> parameters = method.getParameters();
        for (VariableElement parameter : parameters) {
            // Just support for param which type is string
            String paramTypeString = parameter.getEnclosingElement().toString();
            paramTypeString = paramTypeString.substring(paramTypeString.indexOf("(") + 1, paramTypeString.lastIndexOf(")"));
            for (String type : paramTypeString.split(",")) {
                if (!"java.lang.String".equals(type)) {
                    fatalError("Method's params must be String.-> at " + parameter.getEnclosingElement());
                    break;
                }
            }
            customParams.put(parameter.getSimpleName().toString(), parameter.getSimpleName().toString());
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
