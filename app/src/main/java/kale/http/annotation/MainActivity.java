package kale.http.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  APIService api = MainActivity.create(new MyHttpLib()); // 注入API实例
        api.testApi("").subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                Toast.makeText(MainActivity.this, "result" + s, Toast.LENGTH_SHORT).show();
            }
        });*/

        //StringBuilder sb = createString("hahah");
        //Toast.makeText(MainActivity.this, "sb = " + sb.toString(), Toast.LENGTH_SHORT).show();
    }

    public static <T> T createString(String str) {
        Object httpRequestEntity = null;
        try {
            Class<T> httpRequestEntityCls = (Class<T>) Class.forName(StringBuilder.class.getName());
            Constructor con = httpRequestEntityCls.getConstructor(String.class);
            httpRequestEntity = con.newInstance(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) httpRequestEntity;
    }

    public static <T> T create(HttpRequest httpRequest) {
        Object httpRequestEntity = null;
        try {
            Class<T> httpRequestEntityCls = (Class<T>) Class.forName("kale.net.http.HttpRequestEntity");
            Constructor con = httpRequestEntityCls.getConstructor(httpRequest.getClass());
            httpRequestEntity = con.newInstance(httpRequest);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) httpRequestEntity;
    }


}
