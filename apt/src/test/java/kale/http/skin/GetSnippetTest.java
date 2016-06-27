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
public class GetSnippetTest {

    HttpProcessor processor;

    public GetSnippetTest() {
        processor = new HttpProcessor();
    }

    /**
     * custom 0
     * default 0
     */
    @Test
    public void testGet00() throws Exception {
        Map<String, String> customParams = new LinkedHashMap<>();

        String url = "http://www.baidu.com";
        Map<String, String> defaultParams = UrlUtil.getParams(url);
        defaultParams.putAll(customParams);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet00", "",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet00() {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 1
     * default 0
     */
    @Test
    public void testGet10() throws Exception {
        String url = "http://www.baidu.com";

        Map<String, String> customParams = new LinkedHashMap<>();
        put(customParams, "user_id");
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet10", "String user_id",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet10(String user_id) {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=\" + user_id\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 2
     * default 0
     */
    @Test
    public void testGet20() throws Exception {
        Map<String, String> customParams = new LinkedHashMap<>();
        put(customParams, "user_id");
        put(customParams, "test");

        String url = "http://www.baidu.com/?name=kale";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet21", "String user_id, String test",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet21(String user_id, String test) {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=\" + user_id\n"
                + "+ \"&test=\" + test\n"
                + "+ \"&name=kale\"\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 0
     * default 1
     */
    @Test
    public void testGet01() {
        Map<String, String> customParams = new LinkedHashMap<>();
        String url = "http://www.baidu.com?user_id=123";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet01", "",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet01() {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=123\"\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 0
     * default 2
     */
    @Test
    public void testGet02() {
        Map<String, String> customParams = new HashMap<>();
        String url = "http://www.baidu.com/?user_id=123&name=jack";
        Map<String, String> defaultParams = UrlUtil.getParams(url);
        defaultParams.putAll(customParams);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet02", "",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet02() {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=123\"\n"
                + "+ \"&name=jack\"\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 1
     * default 1
     */
    @Test
    public void testGet11() {
        String url = "http://www.baidu.com/?name=jack";

        Map<String, String> customParams = new HashMap<>();
        put(customParams, "user_id");

        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet11", "String user_id",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet11(String user_id) {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=\" + user_id\n"
                + "+ \"&name=jack\"\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 2
     * default 1
     */
    @Test
    public void testGet21() throws Exception {
        Map<String, String> customParams = new LinkedHashMap<>();
        put(customParams, "user_id");
        put(customParams, "test");

        String url = "http://www.baidu.com";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet21", "String user_id, String test",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet21(String user_id, String test) {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=\" + user_id\n"
                + "+ \"&test=\" + test\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 1
     * default 2
     */
    @Test
    public void testGet12() throws Exception {
        Map<String, String> customParams = new LinkedHashMap<>();
        put(customParams, "user_id");

        String url = "http://www.baidu.com/?user_id=123&name=jack";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator
                .createGetSnippet("rx.Observable", "testGet21", "String user_id",
                        "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");

        assertEquals("public rx.Observable testGet21(String user_id) {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=\" + user_id\n"
                + "+ \"&user_id=123\"\n"
                + "+ \"&name=jack\"\n"
                + ", java.lang.Object.class);}", str);
    }

    /**
     * custom 2
     * default 2
     */
    @Test
    public void testGet22() throws Exception {
        Map<String, String> customParams = new LinkedHashMap<>();
        put(customParams, "user_id");
        put(customParams, "test");

        String url = "http://www.baidu.com/?user_id=123&name=jack";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        String str = CodeGenerator.createGetSnippet("rx.Observable", "testGet21", "String user_id, String test",
                "http://www.baidu.com", customParams, defaultParams, "java.lang.Object.class");
        
        assertEquals("public rx.Observable testGet21(String user_id, String test) {\n"
                + "return (rx.Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "+ \"&user_id=\" + user_id\n"
                + "+ \"&test=\" + test\n"
                + "+ \"&user_id=123\"\n"
                + "+ \"&name=jack\"\n"
                + ", java.lang.Object.class);}", str);
    }

    private void put(Map<String, String> map, String param) {
        map.put(param, param);
    }
}