package kale.http.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import kale.net.http.util.HttpReqAdapter;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIService api = HttpReqAdapter.create(new MyHttpLib()); // 注入API实例
        api.testApi("123456789").subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                Toast.makeText(MainActivity.this, "result" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    
}
