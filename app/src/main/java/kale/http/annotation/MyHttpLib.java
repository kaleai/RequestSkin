package kale.http.annotation;

import java.util.HashMap;

import rx.Observable;

/**
 * @author Jack Tony
 * @date 2015/8/17
 * 原本的网络请求框架实现HttpRequest接口即可得到传递过来的请求的数据
 * 这个类模拟的是原先的网络框架
 */
public class MyHttpLib implements HttpRequest {

    /**
     * @param url     请求的url
     * @param <Model> 解析用到的model类的类型
     */
    @Override
    public <Model> Observable doPost(String url) {
        return Observable.just(url);
    }

    /**
     * @param url        请求的url
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable doPost(String url, Class<Model> modelClass) {
        return Observable.just(url);
    }

    /**
     * @param url     请求的url
     * @param hashMap post请求的参数
     * @param <Model> 解析用到的model类的类型
     */
    @Override
    public <Model> Observable doPost(String url, HashMap<String, String> hashMap) {
        return Observable.just(url);
    }

    /**
     * @param url        请求的url
     * @param hashMap    post请求的参数
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable doPost(String url, HashMap<String, String> hashMap, Class<Model> modelClass) {
        return Observable.just(url);
    }

    /**
     * @param url     请求的url
     * @param <Model> 解析用到的model类的类型
     */
    @Override
    public <Model> Observable doGet(String url) {
        return Observable.just(url);
    }

    /**
     * @param url        请求的url
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable doGet(String url, Class<Model> modelClass) {
        return Observable.just(url);
    }
}
