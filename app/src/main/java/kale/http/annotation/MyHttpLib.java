package kale.http.annotation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import kale.net.http.impl.HttpRequest;
import rx.Observable;

/**
 * @author Jack Tony
 * @date 2015/8/17
 * 原本的网络请求框架实现HttpRequest接口即可得到传递过来的请求的数据.
 * 
 * 这个类模拟的是原先的网络框架
 */
public class MyHttpLib implements HttpRequest {

    /**
     * @param url     请求的url
     * @param <Model> 解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doPost(@NonNull String url) {
        return doPost(url, null, null);
    }

    /**
     * @param url        请求的url
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doPost(@NonNull String url, @Nullable Class<Model> modelClass) {
        return doPost(url, null, modelClass);
    }

    /**
     * @param url     请求的url
     * @param map post请求的参数
     * @param <Model> 解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doPost(@NonNull String url, @Nullable HashMap<String, String> map) {
        return doPost(url, map, null);
    }

    /**
     * @param url     请求的url
     * @param <Model> 解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doGet(@NonNull String url) {
        return doGet(url, null);
    }

    // ------------- 真正执行网络请求的方法 -----------
    
    /**
     * @param url        请求的url
     * @param hashMap    post请求的参数
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doPost(String url, HashMap<String, String> hashMap, Class<Model> modelClass) {
        // 首先进行各种判断
        if (url != null) {
            
        }
        for (int i = 0; i < hashMap.size(); i++) {
            
        }
        if (modelClass != null) {
            
        }
        return Observable.just((Model) url); // 这里简单粗暴的把url转换为model类型来做个示例。实际中需要根据解析的model来进行相应的转换。
    }

    /**
     * @param url        请求的url
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doGet(String url, Class<Model> modelClass) {
        return Observable.just((Model)url); // 这里简单粗暴的把url转换为model类型来做个示例。实际中千万不要这么写
    }
}
