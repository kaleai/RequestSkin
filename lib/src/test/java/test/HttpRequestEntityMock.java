package test;

import java.util.HashMap;

import kale.net.http.impl.HttpRequest;
import rx.Observable;

/**
 * 应该生成的类和方法，放在这里做比对。
 */
public class HttpRequestEntityMock implements Cloneable, java.io.Serializable {

    private HttpRequest mHttpRequest;

    public HttpRequestEntityMock(HttpRequest httpRequest) {
        mHttpRequest = httpRequest;
    }

    public Observable testGet00() {
        return (Observable) mHttpRequest.doGet("http://www.baidu.com?"
                , java.lang.Object.class);
    }

    public Observable testGet10(String user_id) {
        return (Observable) mHttpRequest.doGet("http://www.baidu.com?"
                + "&user_id=" + user_id                , java.lang.Object.class);
    }

    public Observable testGet21(String user_id, String test) {
        return (Observable) mHttpRequest.doGet("http://www.baidu.com?"
                + "&user_id=" + user_id
                + "&test=" + test                , java.lang.Object.class);
    }

    public Observable testGet01() {
        return (Observable) mHttpRequest.doGet("http://www.baidu.com?"
                + "&user_id=123"                , java.lang.Object.class);
    }

    public Observable testGet02() {
        return (Observable) mHttpRequest.doGet("http://www.baidu.com?"
                + "&user_id=123"
                + "&name=jack"                , java.lang.Object.class);
    }

    public Observable testGet11(String user_id) {
        return (Observable) mHttpRequest.doGet("http://www.baidu.com?"
                + "&user_id=" + user_id
                + "&name=jack", java.lang.Object.class);
    }

    public Observable testPost00() {
        HashMap<String, String> map = new HashMap<>();

        return (Observable) mHttpRequest.doPost("http://www.baidu.com", map, java.lang.Object.class);
    }

    public Observable testPost01() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "123");

        return (Observable) mHttpRequest.doPost("http://www.baidu.com", map, java.lang.Object.class);
    }

    public Observable testPost02() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "123");
        map.put("name", "jack");

        return (Observable) mHttpRequest.doPost("http://www.baidu.com", map, java.lang.Object.class);
    }

    public Observable testPost10(String user_name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_name", user_name);

        return (Observable) mHttpRequest.doPost("http://www.baidu.com", map, java.lang.Object.class);
    }

    public Observable testPost20(String user_name, String site) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_name", user_name);
        map.put("site", site);

        return (Observable) mHttpRequest.doPost("http://www.baidu.com", map, java.lang.Object.class);
    }

    public Observable testPost11(String user_name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_name", user_name);
        map.put("site", "1");

        return (Observable) mHttpRequest.doPost("http://www.baidu.com", map, java.lang.Object.class);
    }

}


