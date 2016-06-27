package kale.http.example;

import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import kale.http.example.client.MockHttpRequest;
import kale.http.example.client.OkHttpRequest;
import kale.http.example.client.UrlConnectionRequest;
import kale.http.example.model.Result;
import kale.http.example.model.Root;
import kale.http.skin.R;
import kale.http.skin.RequestSkin;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    public static final String BASE_URL = "http://gank.io/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqApiByUrlConnection();
        requestByMockListRequest();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqApiByOkHttp();
            }
        });
    }

    private void requestByMockListRequest() {
        MockHttpApi api = RequestSkin.create(new MockHttpRequest());
        api.testMockGetList().subscribe(new Action1<List<Result>>() {
            @Override
            public void call(final List<Result> resultList) {
                Log.d(TAG, "requestByMockListRequest" + resultList.get(0).desc);
            }
        });
    }

    private void requestByMockRequest() {
        MockHttpApi api = RequestSkin.create(new MockHttpRequest());
        api.testMockGet().subscribe(new Action1<Root>() {
            @Override
            public void call(final Root root) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, root.result.get(0).desc, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void reqApiByOkHttp() {
        OkHttpApi api = RequestSkin.create(new OkHttpRequest());
        api.testOkHttpGet("kale").enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: error");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String s = response.body().string();
                    Root root = new Gson().fromJson(s, Root.class);
                    final String desc = root.result.get(0).desc;
                    Log.d(TAG, "onResponse: " + desc);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void reqApiByUrlConnection() {
        UrlConnectionApi api01 = RequestSkin.create(new UrlConnectionRequest());
        api01.testUrlConnectionGet("just_test").subscribeBy(new Subscriber<Root>() {

            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Root root) {
                final String desc = root.result.get(0).desc;
                Log.d(TAG, "onNext: " + desc);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, desc, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
