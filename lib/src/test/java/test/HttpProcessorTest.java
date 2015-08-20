package test;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import kale.net.http.processor.HttpProcessor;
import kale.net.http.util.UrlUtil;

import static org.junit.Assert.assertEquals;

/**
 * @author Jack Tony
 * @date 2015/8/19
 */
public class HttpProcessorTest {

    HttpProcessor processor;

    public HttpProcessorTest() {
        processor = new HttpProcessor();
    }

    @Test
    public void testCreateClsBlock() throws Exception {
        StringBuilder sb = processor.getStringBuilder();
        sb = processor.createClsBlock("Cloneable", sb);
        sb.append("\n}");

        assertEquals("package kale.net.http;\n"
                + "\n"
                + "import kale.net.http.impl.HttpRequest;\n"
                + "import java.util.HashMap;\n"
                + "import rx.Observable;\n"
                + "\n"
                + "public class HttpRequestEntity implements Cloneable, java.io.Serializable {\n"
                + "\n"
                + "    private HttpRequest mHttpRequest;\n"
                + "\n"
                + "    public HttpRequestEntity(HttpRequest httpRequest) {\n"
                + "        mHttpRequest = httpRequest;\n"
                + "    }\n"
                + "\n"
                + "\n"
                + "\n"
                + "}", sb.toString());
    }

    /**
     * custom 0
     * custom 0
     * @throws Exception
     */
    @Test
    public void testGet00() throws Exception {
        ArrayList<String> customParams = new ArrayList<>();

        String url = "http://www.baidu.com";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createGetMethodBlock("testGet00", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testGet00() {\n"
                + "        return (Observable) mHttpRequest.doGet(\"http://www.baidu.com\", java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }
    
    /**
     * custom 1
     * default 0
     * @throws Exception
     */
    @Test
    public void testGet10() throws Exception {
        ArrayList<String> customParams = new ArrayList<>();
        customParams.add("user_id");

        String url = "http://www.baidu.com";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createGetMethodBlock("testGet10", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testGet10(String user_id) {\n"
                + "        return (Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "                + \"&user_id=\" + user_id                , java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }


    /**
     * custom 2
     * default 1
     * @throws Exception
     */
    @Test
    public void testGet21() throws Exception {
        ArrayList<String> customParams = new ArrayList<>();
        customParams.add("user_id");
        customParams.add("test");
        
        String url = "http://www.baidu.com";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createGetMethodBlock("testGet21", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testGet21(String user_id, String test) {\n"
                + "        return (Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "                + \"&user_id=\" + user_id\n"
                + "                + \"&test=\" + test                , java.lang.Object.class);\n"
                + "    }\n"
                + "\n",sb.toString());
    }

    /**
     * custom 0
     * default 1
     */
    @Test
    public void testGet01() {
        ArrayList<String> customParams = new ArrayList<>();

        String url = "http://www.baidu.com?user_id=123";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createGetMethodBlock("testGet01", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testGet01() {\n"
                + "        return (Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "                + \"&user_id=123\"                , java.lang.Object.class);\n"
                + "    }\n"
                + "\n",sb.toString());
    }

    /**
     * custom 0
     * default 2
     */
    @Test
    public void testGet02() {
        ArrayList<String> customParams = new ArrayList<>();

        String url = "http://www.baidu.com/?user_id=123&name=jack";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createGetMethodBlock("testGet02", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testGet02() {\n"
                + "        return (Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "                + \"&user_id=123\"\n"
                + "                + \"&name=jack\"                , java.lang.Object.class);\n"
                + "    }\n"
                + "\n",sb.toString());
    }

    /**
     * custom 1
     * default 1
     */
    @Test
    public void testGet11() {
        ArrayList<String> customParams = new ArrayList<>();
        customParams.add("user_id");
        
        String url = "http://www.baidu.com/?name=jack";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createGetMethodBlock("testGet11", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testGet11(String user_id) {\n"
                + "        return (Observable) mHttpRequest.doGet(\"http://www.baidu.com?\"\n"
                + "                + \"&user_id=\" + user_id\n"
                + "                + \"&name=jack\"                , java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }


    /**
     * custom 0
     * default 0
     */
    @Test
    public void testPost00() {
        HttpProcessor processor = new HttpProcessor();

        ArrayList<String> customParams = new ArrayList<>();

        String url = "http://www.baidu.com/";
        Map<String, String> defaultParams = UrlUtil.getParams(url);
        
        StringBuilder sb = processor.createPostMethodBlock("testPost00", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testPost00() {\n"
                + "        return (Observable) mHttpRequest.doPost(\"http://www.baidu.com\", java.lang.Object.class);\n"
                + "    }\n"
                + "\n",sb.toString());
    }

    @Test
    public void testPost01() {
        HttpProcessor processor = new HttpProcessor();

        ArrayList<String> customParams = new ArrayList<>();

        String url = "http://www.baidu.com/?id=123";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createPostMethodBlock("testPost01", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testPost01() {\n"
                + "        HashMap<String, String> map = new HashMap<>();\n"
                + "        map.put(\"id\", \"123\");\n"
                + "\n"
                + "        return (Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map, java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }

    @Test
    public void testPost02() {
        HttpProcessor processor = new HttpProcessor();

        ArrayList<String> customParams = new ArrayList<>();

        String url = "http://www.baidu.com/?id=123&name=jack";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createPostMethodBlock("testPost02", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testPost02() {\n"
                + "        HashMap<String, String> map = new HashMap<>();\n"
                + "        map.put(\"name\", \"jack\");\n"
                + "        map.put(\"id\", \"123\");\n"
                + "\n"
                + "        return (Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map, java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }

    @Test
    public void testPost10() {
        HttpProcessor processor = new HttpProcessor();

        ArrayList<String> customParams = new ArrayList<>();
        customParams.add("user_name");
        String url = "http://www.baidu.com/";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createPostMethodBlock("testPost10", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testPost10(String user_name) {\n"
                + "        HashMap<String, String> map = new HashMap<>();\n"
                + "        map.put(\"user_name\", user_name);\n"
                + "\n"
                + "        return (Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map, java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }

    @Test
    public void testPost20() {
        HttpProcessor processor = new HttpProcessor();

        ArrayList<String> customParams = new ArrayList<>();
        customParams.add("user_name");
        customParams.add("site");
        String url = "http://www.baidu.com/";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createPostMethodBlock("testPost20", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testPost20(String user_name, String site) {\n"
                + "        HashMap<String, String> map = new HashMap<>();\n"
                + "        map.put(\"user_name\", user_name);\n"
                + "        map.put(\"site\", site);\n"
                + "\n"
                + "        return (Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map, java.lang.Object.class);\n"
                + "    }\n"
                + "\n",sb.toString());
    }

    @Test
    public void testPost11() {
        HttpProcessor processor = new HttpProcessor();

        ArrayList<String> customParams = new ArrayList<>();
        customParams.add("user_name");
        String url = "http://www.baidu.com/?site=1";
        Map<String, String> defaultParams = UrlUtil.getParams(url);

        StringBuilder sb = processor.createPostMethodBlock("testPost11", "http://www.baidu.com", defaultParams, customParams, Object.class.getName());
        assertEquals("    public Observable testPost11(String user_name) {\n"
                + "        HashMap<String, String> map = new HashMap<>();\n"
                + "        map.put(\"user_name\", user_name);\n"
                + "        map.put(\"site\", \"1\");\n"
                + "\n"
                + "        return (Observable) mHttpRequest.doPost(\"http://www.baidu.com\", map, java.lang.Object.class);\n"
                + "    }\n\n",sb.toString());
    }



}