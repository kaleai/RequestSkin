package kale.http.example.client;

import android.util.Log;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import kale.http.skin.HttpRequest;
import kale.http.example.MainActivity;
import okhttp3.Request;

/**
 * @author Kale
 * @date 2016/6/11
 */
public class OkHttpRequest implements HttpRequest {

    private static final String TAG = "OkHttpRequest";

    private okhttp3.OkHttpClient client;
    
    public OkHttpRequest() {
        client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public <T> Object doPost(String url, Map<String, String> map, Class<T> modelClass) {
        throw new UnsupportedOperationException("post request are not implement");
    }

    @Override
    public <T> Object doGet(String url, Class<T> modelClass) {
        url = MainActivity.BASE_URL + url;

        Log.d(TAG, "doGet: url = " + url);
        
        final Request request = new Request.Builder()
                .url(url)
                .build();

        return client.newCall(request);
    }
}
