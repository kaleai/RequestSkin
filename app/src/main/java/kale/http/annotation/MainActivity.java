package kale.http.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kale.net.http.util.HttpReqAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APIService api = HttpReqAdapter.create(new MyHttpLib()); // 注入API实例
    }
}
