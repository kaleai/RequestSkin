package kale.http.skin;

import java.util.Map;

/**
 * @author Jack Tony
 * @date 2015/10/8
 */
public class CodeGenerator {

    static String createGetSnippet(String methodType,String methodName,String paramsStr,
            String url, Map<String, String> customParams, Map<String, String> paramsFromUrl, String modelName) {

        StringBuilder paramSb = new StringBuilder();

        for (Map.Entry<String, String> p : customParams.entrySet()) {
            paramSb.append("+ \"&").append(p.getKey()).append("=\" + ").append(p.getValue()).append("\n");
        }
        for (Map.Entry<String, String> p : paramsFromUrl.entrySet()) {
            paramSb.append("+ \"&").append(p).append("\"\n");
        }

        return Phrase.from("public {type} {name}({params_str}) {{\n"
                + "return ({type}) mHttpRequest.doGet({url}\n"
                + "{param_value}, {model_class});}")

                .put("type", methodType)
                .put("name", methodName)
                .put("params_str", paramsStr)
                .put("url", "\"" + url + "?\"")
                .put("param_value", paramSb.toString())
                .put("model_class", modelName != null ? modelName + ".class" : "null")
                .format();
    }

    static String createPostSnippet(String methodType, String methodName, String paramsStr,
            String url, Map<String, String> customParams, Map<String, String> paramsFromUrl, String modelName) {

        // create map block
        StringBuilder mapSb = new StringBuilder();
        for (Map.Entry<String, String> p : customParams.entrySet()) {
            mapSb.append(Phrase.from("map.put(\"{key}\", String.valueOf({value}));\n")
                    .put("key", p.getKey())
                    .put("value", p.getValue())
                    .format());
        }
        for (Map.Entry<String, String> p : paramsFromUrl.entrySet()) {
            mapSb.append(Phrase.from("map.put(\"{key}\", \"{value}\");\n")
                    .put("key", p.getKey())
                    .put("value", p.getValue())
                    .format());
        }

        return Phrase.from("public {type} {name}({params_str}) {{\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "{param_value}\n"
                + "return ({type}) mHttpRequest.doPost({url}, map, {model_class});}")
                .put("type", methodType)
                .put("name", methodName)
                .put("params_str", paramsStr)
                .put("param_value", mapSb.toString())
                .put("url", "\"" + url + "\"")
                .put("model_class", modelName != null ? modelName + ".class" : "null")
                .format();
    }

}
