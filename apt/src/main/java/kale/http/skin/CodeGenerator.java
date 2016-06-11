package kale.http.skin;

import java.util.Map;

/**
 * @author Jack Tony
 * @date 2015/10/8
 */
public class CodeGenerator {

    static String createGetSnippet(String methodName, String methodType,
            String url, Map<String, String> customParams, Map<String, String> paramsFromUrl, String modelName) {

        StringBuilder paramSb = new StringBuilder();

        for (Map.Entry<String, String> p : customParams.entrySet()) {
            paramSb.append("+ \"&").append(p.getKey()).append("=\" + ").append(p.getValue()).append("\n");
        }
        for (Map.Entry<String, String> p : paramsFromUrl.entrySet()) {
            paramSb.append("+ \"&").append(p).append("\"\n");
        }

        return Phrase.from("public {type} {name}({string_params}) {{\n"
                + "return ({type}) mHttpRequest.doGet({url}\n"
                + "{param_value}, {model_class});}")

                .put("type", methodType)
                .put("name", methodName)
                .put("string_params", params2String(customParams))
                .put("url", "\"" + url + "?\"")
                .put("param_value", paramSb.toString())
                .put("model_class", modelName != null ? modelName + ".class" : "null")
                .format();
    }

    static String createPostSnippet(String methodName, String methodType,
            String url, Map<String, String> customParams, Map<String, String> paramsFromUrl, String modelName) {

        // create map block
        StringBuilder mapSb = new StringBuilder();
        for (Map.Entry<String, String> p : customParams.entrySet()) {
            mapSb
                    .append("map.put(" + "\"").append(p.getKey()).append("\"")
                    .append(", ").append(p.getValue()).append(");").append("\n");
        }
        for (Map.Entry<String, String> p : paramsFromUrl.entrySet()) {
            mapSb
                    .append("map.put(" + "\"")
                    .append(p.getKey()).append("\"")
                    .append(", \"").append(p.getValue()).append("\");").append("\n");
        }

        return Phrase.from("public {type} {name}({string_params}) {{\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "{param_value}\n"
                + "return ({type}) mHttpRequest.doPost({url}, map, {model_class});}")
                .put("type", methodType)
                .put("name", methodName)
                .put("string_params", params2String(customParams))
                .put("param_value", mapSb.toString())
                .put("url", "\"" + url + "\"")
                .put("model_class", modelName != null ? modelName + ".class" : "null")
                .format();
    }

    /**
     * Map<String,String> -> String a, String b
     */
    private static String params2String(Map<String, String> customParams) {
        StringBuilder paramsSb = new StringBuilder();
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            paramsSb.append("String ").append(entry.getKey()).append(", ");
        }

        if (paramsSb.length() != 0) {
            // finally delete last ','
            paramsSb.deleteCharAt(paramsSb.length() - 1).deleteCharAt(paramsSb.length() - 1);
        }
        return paramsSb.toString();
    }

}
