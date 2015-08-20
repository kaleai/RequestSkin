package kale.net.http.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import kale.net.http.annotation.ApiInterface;
import kale.net.http.annotation.HttpGet;
import kale.net.http.annotation.HttpPost;
import kale.net.http.impl.HttpRequest;
import kale.net.http.util.UrlUtil;


/**
 * @author Jack Tony
 * @date 2015/8/16
 */
@SupportedAnnotationTypes({"kale.net.http.annotation.HttpGet", "kale.net.http.annotation.HttpPost", "kale.net.http.annotation.ApiInterface"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class HttpProcessor extends AbstractProcessor {

    private static final String TAG = "[ " + HttpProcessor.class.getSimpleName() + " ]:";


    public static final String PACKAGE_NAME = "kale.net.http";

    public static final String CLASS_NAME = "HttpRequestEntity";

    private String PARENT_CLASS_NAME = "java.io.Serializable";

    private Elements elementUtils;

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
                        //isPost = true;
                        handlerHttp(mStringBuilder, e, method, true);
                    } else {
                        //isPost = false;
                        handlerHttp(mStringBuilder, e, method, false);
                    }
                }

            }
        }
        mStringBuilder.append("\n}");
        createClassFile(PACKAGE_NAME, CLASS_NAME, mStringBuilder.toString());
        return true;
    }

    public StringBuilder createClsBlock(String interFaceName,StringBuilder sb) {
        int start = sb.indexOf(PARENT_CLASS_NAME);
        return sb.insert(start, interFaceName + ", ");
    }

    public void handlerHttp(StringBuilder sb, Element ele, ExecutableElement method, boolean isPost) {
        String url;
        String modelName;
        if (isPost) {
            HttpPost httpPost = ele.getAnnotation(HttpPost.class);
            url = httpPost.url();
            try {
                modelName = httpPost.model().getName();
            } catch (MirroredTypeException ex) {
                modelName = ex.getTypeMirror().toString();
            }
        } else {
            HttpGet httpGet = ele.getAnnotation(HttpGet.class);
            url = httpGet.url();
            try {
                modelName = httpGet.model().getName();
            } catch (MirroredTypeException ex) {
                modelName = ex.getTypeMirror().toString();
            }
        }

        if (url.equals("")) {
            fatalError("Url is null");
            return;
        }
        log("Working on method: " + method.getSimpleName());
        Map<String, String> defaultParams = UrlUtil.getParams(url);
        List<String> customParams = getCustomParams(method);
        if (modelName.equals(HttpProcessor.class.getName())) {
            modelName = null;
        }

        if (modelName != null && modelName.contains("<any?>")) {
            modelName = modelName.replace("<any?>", UrlUtil.url2packageName(url));
        }
        url = UrlUtil.getRealUrl(url);
        if (isPost) {
            sb.append(createPostMethodBlock(method.getSimpleName().toString(), url, defaultParams, customParams, modelName));
        } else {
            sb.append(createGetMethodBlock(method.getSimpleName().toString(), url, defaultParams, customParams, modelName));
        }
        log("Parse method: " + method.getSimpleName() + " completed");
    }


    private List<String> getCustomParams(ExecutableElement method) {
        List<String> customParams = new ArrayList<String>();
        List<? extends VariableElement> parameters = method.getParameters();
        for (VariableElement parameter : parameters) {
            customParams.add(parameter.getSimpleName().toString());
        }
        return customParams;
    }

    public StringBuilder createPostMethodBlock( String methodName,  String url,
             Map<String, String> defaultParams,  List<String> customParams, String modelName) {

        StringBuilder sb = new StringBuilder();
        if (customParams.size() == 0 && defaultParams.size() == 0) {
            String functionBlockStr =
                    "    public Observable " + methodName + "() {\n"
                            + "        return (Observable) mHttpRequest.doPost({url}, {cls});\n"
                            + "    }\n\n";
            functionBlockStr = functionBlockStr.replace("{url}", "\"" + url + "\"");
            if (modelName != null) {
                functionBlockStr = functionBlockStr.replace("{cls}", modelName + ".class");
            } else {
                functionBlockStr = functionBlockStr.replace(", {cls}", "");
            }
            return sb.append(functionBlockStr);
        }
        else {
            String functionBlockStr =
                    "    public Observable " + methodName + "({params}) {\n"
                            + "        HashMap<String, String> map = new HashMap<>();\n"
                            + "        {(map.put(��)}\n"
                            + "        return (Observable) mHttpRequest.doPost({url}, map, {cls});\n"
                            + "    }\n\n";
            if (customParams.size() == 0) {
                functionBlockStr = functionBlockStr.replace("{params}", "");
            } else {
                StringBuilder paramsSb = new StringBuilder();
                for (String customParam : customParams) {
                    paramsSb.append("String ").append(customParam).append(", ");
                }
                paramsSb.deleteCharAt(paramsSb.length() - 1);
                paramsSb.deleteCharAt(paramsSb.length() - 1);
                functionBlockStr = functionBlockStr.replace("{params}", paramsSb.toString());
            }

            // �滻mapֵ
            StringBuilder mapSb = new StringBuilder();
            for (String customParam : customParams) {
                mapSb.append("        ")
                        .append("map.put(" + "\"").append(customParam).append("\"").append(", ").append(customParam).append(");").append("\n");
            }
            for (Map.Entry<String, String> defaultParam : defaultParams.entrySet()) {
                mapSb.append("        ")
                        .append("map.put(" + "\"")
                        .append(defaultParam.getKey()).append("\"").append(", \"").append(defaultParam.getValue()).append("\");")
                        .append("\n");
            }
            functionBlockStr = functionBlockStr.replace("        {(map.put(��)}", mapSb.toString());

            functionBlockStr = functionBlockStr.replace("{url}", "\"" + url + "\"");
            if (modelName != null) {
                functionBlockStr = functionBlockStr.replace("{cls}", modelName + ".class");
            } else {
                functionBlockStr = functionBlockStr.replace(", {cls}", "");
            }
            return sb.append(functionBlockStr);
        }
    }

    public StringBuilder createGetMethodBlock( String methodName,  String url,
             Map<String, String> defaultParams,  List<String> customParams, String modelName) {

        StringBuilder sb = new StringBuilder();
        if (customParams.size() == 0 && defaultParams.size() == 0) {
            String functionBlockStr =
                    "    public Observable " + methodName + "() {\n"
                            + "        return (Observable) mHttpRequest.doGet({url}, {cls});\n"
                            + "    }\n\n";

            functionBlockStr = functionBlockStr.replace("{url}", "\"" + url + "\"");
            if (modelName != null) {
                functionBlockStr = functionBlockStr.replace("{cls}", modelName + ".class");
            } else {
                functionBlockStr = functionBlockStr.replace(", {cls}", "");

            }
            return sb.append(functionBlockStr);
        }
        else {
            String functionBlockStr =
                    "    public Observable " + methodName + "({params}) {\n"
                            + "        return (Observable) mHttpRequest.doGet({url}\n"
                            + "                {param=value}                , {cls});\n"
                            + "    }\n\n";
            if (customParams.size() == 0) {
                functionBlockStr = functionBlockStr.replace("{params}", "");
            } else {
                StringBuilder paramSb = new StringBuilder();
                for (String customParam : customParams) {
                    paramSb.append("String ").append(customParam).append(", ");
                }
                paramSb.deleteCharAt(paramSb.length() - 1);
                paramSb.deleteCharAt(paramSb.length() - 1);
                functionBlockStr = functionBlockStr.replace("{params}", paramSb.toString());
            }
            StringBuilder paramSb = new StringBuilder();
            for (String customParam : customParams) {
                paramSb.append("                + \"&").append(customParam).append("=\" + ").append(customParam).append("\n");
            }
            for (Map.Entry<String, String> defaultParam : defaultParams.entrySet()) {
                paramSb.append("                + \"&").append(defaultParam.getKey()).append("=").append(defaultParam.getValue()).append("\"\n");
            }
            paramSb.deleteCharAt(paramSb.length() - 1);
            functionBlockStr = functionBlockStr.replace("                {param=value}", paramSb.toString());

            functionBlockStr = functionBlockStr.replace("{url}", "\"" + url + "?\"");
            if (modelName != null) {
                functionBlockStr = functionBlockStr.replace("{cls}", modelName + ".class");
            } else {
                functionBlockStr = functionBlockStr.replace("                , {cls}", "");
            }
            return sb.append(functionBlockStr);
        }
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
            e.printStackTrace();
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
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, TAG + " FATAL ERROR: " + msg);
    }

}
