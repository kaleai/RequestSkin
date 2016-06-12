package kale.http.skin;


import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Jack Tony
 * @date 2015/8/19
 */
public class PostSnippetTest {

    HttpProcessor processor;

    public PostSnippetTest() {
        processor = new HttpProcessor();
    }

    /**
     * custom 0
     * default 0
     */
    @Test
    public void testPost00() {
        String url = "http://www.baidu.com/";
        Map<String, String> customParams = new HashMap<>();
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator.createPostSnippet("rx.Observable", "testPost00", "",
                "http://www.baidu.com", customParams, defaultParams, Object.class.getName());

        assertEquals("public rx.Observable testPost00() {\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "\n"
                + "return (rx.Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 0
     * default 1
     */
    @Test
    public void testPost01() {
        String url = "http://www.baidu.com/?id=123";
        Map<String, String> customParams = new HashMap<>();
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator.createPostSnippet("rx.Observable", "testPost01", "",
                "http://www.baidu.com", customParams, defaultParams, Object.class.getName());

        assertEquals("public rx.Observable testPost01() {\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "map.put(\"id\", \"123\");\n"
                + "\n"
                + "return (rx.Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 0
     * default 2
     */
    @Test
    public void testPost02() {
        String url = "http://www.baidu.com/?id=123&name=jack";
        Map<String, String> customParams = new HashMap<>();
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String sb = CodeGenerator.createPostSnippet("rx.Observable", "testPost02", "",
                "http://www.baidu.com", customParams, defaultParams, Object.class.getName());

        assertEquals("public rx.Observable testPost02() {\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "map.put(\"id\", \"123\");\n"
                + "map.put(\"name\", \"jack\");\n"
                + "\n"
                + "return (rx.Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map"
                + ", java.lang.Object.class);}", sb);
    }

    /**
     * custom 1
     * default 0
     */
    @Test
    public void testPost10() {
        Map<String, String> customParams = new HashMap<>();
        put(customParams, "user_name");

        String url = "http://www.baidu.com/";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator.createPostSnippet("rx.Observable", "testPost10", "String user_name",
                "http://www.baidu.com", customParams, defaultParams, Object.class.getName());

        assertEquals("public rx.Observable testPost10(String user_name) {\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "map.put(\"user_name\", String.valueOf(user_name));\n"
                + "\n"
                + "return (rx.Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 2
     * default 0
     */
    @Test
    public void testPost20() {
        Map<String, String> customParams = new LinkedHashMap<>();
        put(customParams, "user_name");
        put(customParams, "site");

        String url = "http://www.baidu.com/";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator.createPostSnippet("rx.Observable", "testPost20",
                "String user_name, String site", "http://www.baidu.com",
                customParams, defaultParams, Object.class.getName());

        assertEquals("public rx.Observable testPost20(String user_name, String site) {\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "map.put(\"user_name\", String.valueOf(user_name));\n"
                + "map.put(\"site\", String.valueOf(site));\n"
                + "\n"
                + "return (rx.Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 1
     * default 1
     */
    @Test
    public void testPost11() {
        Map<String, String> customParams = new HashMap<>();
        put(customParams, "user_name");

        String url = "http://www.baidu.com/?site=1";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator.createPostSnippet("rx.Observable", "testPost11", "String user_name",
                "http://www.baidu.com", customParams, defaultParams, Object.class.getName());

        assertEquals("public rx.Observable testPost11(String user_name) {\n"
                + "Map<String, String> map = new ArrayMap<>();\n"
                + "map.put(\"user_name\", String.valueOf(user_name));\n"
                + "map.put(\"site\", \"1\");\n"
                + "\n"
                + "return (rx.Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map"
                + ", java.lang.Object.class);}", str);
    }

    private void put(Map<String, String> map, String param) {
        map.put(param, param);
    }

}