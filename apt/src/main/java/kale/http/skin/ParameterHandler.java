package kale.http.skin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import kale.http.skin.annotation.parameter.Part;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by YLM on 2017/8/16.
 */
abstract class ParameterHandler {

    protected String url;

    protected String mthodName;

    protected String returnType;

    protected String methodParamsStr;

    protected String returnMethod;

    protected String parameter;

    protected String paramValue;

    protected String modelName;

    protected ExecutableElement method;

    protected Phrase phrase  = Phrase
            .from("    public {type} {name}({params_str}) {{\n"
                + "{param_value}\n"
                + "        return ({type}) mHttpRequest.{return_method}(\"{url}\", {parameter}, {model_class});\n    }\n");

    abstract String apply();

    abstract String parseParamValue(List<? extends VariableElement> parameters);

    abstract String parseReturnParam();

    protected String getParamsString(ExecutableElement method) {
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

    static final class Parts extends ParameterHandler{

        public Parts(ExecutableElement method,List<? extends VariableElement> parameters,String returnMethod,String url){
            this.method = method;
            this.url = url;
            this.mthodName = method.getSimpleName().toString();
            this.returnType = method.getReturnType().toString();
            this.methodParamsStr = getParamsString(method);
            this.returnMethod = returnMethod;
            this.paramValue = parseParamValue(parameters);
            this.parameter = parseReturnParam();
            this.modelName = getModelName(url,returnType);
        }

        @Override
        String apply() {
            return phrase.put("url",url)
                    .put("type",returnType)
                    .put("name",mthodName)
                    .put("params_str",methodParamsStr)
                    .put("return_method",returnMethod)
                    .put("parameter",parameter)
                    .put("param_value",paramValue)
                    .put("model_class",modelName)
                    .format();
        }

        @Override
        String parseParamValue(List<? extends VariableElement> parameters) {
            //File
            StringBuilder sb = new StringBuilder();
            sb.append("        MultipartBody body = new MultipartBody.Builder()\n" +
                    "                .setType(MultipartBody.FORM)\n");
            for (VariableElement parameter : parameters){
                if (parameter.getAnnotation(Part.class) != null){
                    addPart(sb,parameter);
                }
            }
            sb.append("                .build();\n");
            return sb.toString();
        }

        @Override
        String parseReturnParam() {
            return "body";
        }

        private void addPart(StringBuilder sb,VariableElement ve){
            if (ve.asType().toString().equals(File.class.getCanonicalName())){
                sb.append(Phrase.from("                .addFormDataPart({name}, {filename},\n" +
                        "                    RequestBody.create(MEDIA_TYPE_PNG, {file}))\n")
                        .put("file",ve.getSimpleName().toString())
                        .put("name","\""+ve.getAnnotation(Part.class).value().toString()+"\"")
                        .put("filename",(ve.getAnnotation(Part.class).filename().isEmpty()?
                                ve.getSimpleName().toString()+".getName()":"\""+ve.getAnnotation(Part.class).filename()+"\""))
                        .format());
                sb.insert(0,"        MediaType MEDIA_TYPE_PNG = MediaType.parse(\""+ve.getAnnotation(Part.class).mediaType()+"\");\n");
            }else if (ve.asType().toString().equals(MultipartBody.Part.class.getCanonicalName())){
                sb.append(Phrase.from("                .addPart({part})\n")
                        .put("part",ve.getSimpleName().toString())
                        .format());
            }else if (ve.asType().toString().equals(RequestBody.class.getCanonicalName())){
                sb.append(Phrase.from("                .addFormDataPart({name}, {filename},\n" +
                        "                    {request_body})\n")
                        .put("request_body",ve.getSimpleName().toString())
                        .put("name","\""+ve.getAnnotation(Part.class).value()+"\"")
                        .put("filename","\""+ve.getAnnotation(Part.class).filename() +"\"")
                        .format());
            }else if (ve.asType().toString().equals(String.class.getCanonicalName())){
                sb.append(Phrase.from("                .addFormDataPart({name}, {value},\n")
                        .put("name","\""+ve.getAnnotation(Part.class).value()+"\"")
                        .put("value",ve.getSimpleName().toString())
                        .format());
            }

        }
    }

    static final class Default extends ParameterHandler{

        @Override
        String apply() {
            return "//default";
        }

        @Override
        String parseParamValue(List<? extends VariableElement> parameters) {
            return null;
        }

        @Override
        String parseReturnParam() {
            return null;
        }
    }
}
