package kale.http.example.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.lang.reflect.Type;
import java.util.Map;

import kale.http.skin.HttpRequest;
import okhttp3.MultipartBody;
import rx.Observable;

/**
 * @author Jack Tony
 * @date 2015/8/17
 * 原本的网络请求框架实现HttpRequest接口即可得到传递过来的请求的数据.
 *
 * 这个类模拟的是原先的网络框架
 */
public class ExampleHttpRequest implements HttpRequest {

    /**
     * @param url        请求的url
     * @param hashMap    post请求的参数
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doPost(@NonNull String url, @Nullable Map<String, String> hashMap,
            @Nullable Class<Model> modelClass) {

        // 首先进行各种判断
        if (TextUtils.isEmpty(url)) {

        }
        if (hashMap != null) {
            for (int i = 0; i < hashMap.size(); i++) {

            }
        }
        if (modelClass != null) {

        }
        return Observable.just((Model) url); // 这里简单粗暴的把url转换为model类型来做个示例。实际中需要根据解析的model来进行相应的转换。
    }

    @Override
    public <Model> Observable<Model> doPost(String url, Map<String, String> map, Type type) {
        return null;
    }

    /**
     * @param url        请求的url
     * @param modelClass 解析用到的model的class对象。比如：String.class
     * @param <Model>    解析用到的model类的类型
     */
    @Override
    public <Model> Observable<Model> doGet(String url, @Nullable Class<Model> modelClass) {
        if (TextUtils.isEmpty(url)) {

        }
        return Observable.just((Model) url); // 这里简单粗暴的把url转换为model类型来做个示例。实际中千万不要这么写
    }

    @Override
    public <Model> Observable<Model> doGet(String url, Type type) {
        return null;
    }

    @Override
    public <T> Object doPostMultipart(String url, MultipartBody body, Class<T> modelClass) {
        return null;
    }

}
