package kale.net.http.util;

import java.util.Map;

/**
 * @author Jack Tony
 * @date 2015/10/8
 */
public class HttpCodeSnippetUtil {

    public static String createPostSnippet(String methodName, String url, Map<String, String> customParams, Map<String, String> defaultParams,
            String modelName) {
        String functionBlockStr =
                "    public Observable " + methodName + "({params}) {\n"
                        + "        HashMap<String, String> map = new HashMap<>();\n"
                        + "        {(map.put(...)}\n"
                        + "        return (Observable) mHttpRequest.doPost({url}, map, {cls});\n"
                        + "    }\n\n";

        functionBlockStr = functionBlockStr.replace("{params}", getStringParams(customParams));
        
        // create map block
        StringBuilder mapSb = new StringBuilder();
        for (Map.Entry<String, String> customParam : customParams.entrySet()) {
            mapSb.append("        ")
                    .append("map.put(" + "\"").append(customParam.getKey()).append("\"")
                    .append(", ").append(customParam.getValue()).append(");").append("\n");
        }
        for (Map.Entry<String, String> defaultParam : defaultParams.entrySet()) {
            mapSb.append("        ")
                    .append("map.put(" + "\"")
                    .append(defaultParam.getKey()).append("\"")
                    .append(", \"").append(defaultParam.getValue()).append("\");").append("\n");
        }
        
        return functionBlockStr
                .replace("        {(map.put(...)}", mapSb.toString())
                .replace("{url}", "\"" + url + "\"")
                .replace("{cls}", modelName != null ? modelName + ".class" : "null");
    }


    public static String createGetSnippet(String methodName, String url, Map<String, String> customParams, Map<String, String> defaultParams, String
            modelName) {
        String functionBlockStr =
                "    public Observable " + methodName + "({params}) {\n"
                        + "        return (Observable) mHttpRequest.doGet({url}\n"
                        + "                {param=value}                , {cls});\n"
                        + "    }\n\n";
        functionBlockStr = functionBlockStr.replace("{params}", getStringParams(customParams).toString());

        StringBuilder paramSb = new StringBuilder();
        for (Map.Entry<String, String> customParam : customParams.entrySet()) {
            paramSb.append("                + \"&").append(customParam.getKey()).append("=\" + ").append(customParam.getValue()).append("\n");
        }
        for (Map.Entry<String, String> defaultParam : defaultParams.entrySet()) {
            paramSb.append("                + \"&").append(defaultParam).append("\"\n");
        }
        if (paramSb.length() != 0) {
            paramSb.deleteCharAt(paramSb.length() - 1);
        }

        return functionBlockStr
                .replace("                {param=value}", paramSb)
                .replace("{url}", "\"" + url + "?\"")
                .replace("{cls}", modelName != null ? modelName + ".class" : "null");
    }

    private static StringBuilder getStringParams(Map<String, String> customParams) {
        StringBuilder paramsSb = new StringBuilder();
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            paramsSb.append("String ").append(entry.getKey()).append(", ");
        }
        if (paramsSb.length() != 0) {
            paramsSb.deleteCharAt(paramsSb.length() - 1).deleteCharAt(paramsSb.length() - 1);
        }
        return paramsSb;
    }
    
}
