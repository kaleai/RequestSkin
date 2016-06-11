package kale.http.example.client;

import com.google.gson.Gson;

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import kale.http.skin.HttpRequest;
import kale.http.example.MainActivity;
import rx.Subscriber;

/**
 * @author Kale
 * @date 2016/6/11
 */
public class UrlConnectionRequest implements HttpRequest {

    private static final String TAG = "UrlConnectionRequest";

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    @Override
    public <T> Object doPost(String url, Map<String, String> map, Class<T> modelClass) {
        throw new UnsupportedOperationException("post request are not implement");
    }

    @Override
    public <T> Object doGet(String url, Class<T> modelClass) {
        url = MainActivity.BASE_URL + url;
        final Call<T> callBack = new Call<>(modelClass);

        final String finalUrl = url;
        Log.d(TAG, "doGet: url = " + url);

        new Thread() {
            public void run() {
                String result = doGetAsync(finalUrl);
                if (result != null) {
                    callBack.onRequestComplete(result);
                } else {
                    callBack.onError();
                }
            }
        }.start();
        return callBack;
    }

    /**
     * Get请求，获得返回数据
     */
    public static String doGetAsync(String urlStr) {
        URL url;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException ignore) {
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }


    public static class Call<T> {

        private Gson gson = new Gson();

        private T obj;

        private Class<T> clz;

        private Subscriber<T> subscribe;

        public Call(Class<T> clz) {
            this.clz = clz;
        }

        public void onRequestComplete(String result) {
            obj = gson.fromJson(result, clz);
            if (obj != null) {
                subscribe.onNext(obj);
            } else {
                subscribe.onError(new NetworkErrorException());
            }
        }

        public void onError() {
            subscribe.onError(new NetworkErrorException());
        }

        public void subscribeBy(Subscriber<T> subscribe) {
            this.subscribe = subscribe;
        }
    }
}
