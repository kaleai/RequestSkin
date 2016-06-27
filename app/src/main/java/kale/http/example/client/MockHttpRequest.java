package kale.http.example.client;

import com.google.gson.Gson;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.Map;

import kale.http.skin.HttpRequest;
import kale.http.example.MainActivity;
import rx.Observable;

/**
 * @author Kale
 * @date 2016/6/11
 */
public class MockHttpRequest implements HttpRequest {

    private static final String TAG = "MockHttpRequest";

    private static final String MOCK_JSON =
            "{\n"
                    + "  \"error\": false,\n"
                    + "  \"results\": [\n"
                    + "    {\n"
                    + "      \"_id\": \"56cc6d1c421aa95caa7074fc\",\n"
                    + "      \"createdAt\": \"2015-06-10T04:45:12.150Z\",\n"
                    + "      \"desc\": \"为Android提供不可改变的数据集支持\",\n"
                    + "      \"publishedAt\": \"2015-06-11T03:30:41.4Z\",\n"
                    + "      \"type\": \"Android\",\n"
                    + "      \"url\": \"https://github.com/konmik/solid\",\n"
                    + "      \"used\": true,\n"
                    + "      \"who\": \"mthli\"\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}";

    private static final String MOCK_JSON_LIST = "[\n"
            + "    {\n"
            + "      \"_id\": \"56cc6d1c421aa95caa70751a\",\n"
            + "      \"createdAt\": \"2015-11-05T02:42:01.464Z\",\n"
            + "      \"desc\": \"一个Android Permissions Checker\",\n"
            + "      \"publishedAt\": \"2015-11-05T04:02:52.971Z\",\n"
            + "      \"type\": \"Android\",\n"
            + "      \"url\": \"https://github.com/ZeroBrain/AndroidPermissions\",\n"
            + "      \"used\": true,\n"
            + "      \"who\": \"有时放纵\"\n"
            + "    }]";

    @Override
    public <T> Object doPost(String url, Map<String, String> map, Class<T> modelClass) {
        throw new UnsupportedOperationException("post request are not implement");
    }

    @Override
    public <T> Object doPost(String url, Map<String, String> map, Type type) {
        throw new UnsupportedOperationException("post request are not implement");
    }

    @Override
    public <T> Object doGet(String url, Class<T> modelClass) {
        url = MainActivity.BASE_URL + url;
        Log.d(TAG, "doGet: url = " + url);

        return Observable.just(new Gson().fromJson(MOCK_JSON, modelClass));
    }

    @Override
    public Object doGet(String url, Type type) {
        url = MainActivity.BASE_URL + url;
        Log.d(TAG, "doGet: url = " + url);

        return Observable.just(new Gson().fromJson(MOCK_JSON_LIST, type));
    }
}
