package kale.http.annotation;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kale.net.http.util.HttpReqAdapter;

public class MainActivity extends AppCompatActivity {


    private class ActivityLifeCallbacks implements Application.ActivityLifecycleCallbacks {


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIService api = HttpReqAdapter.create(new MyHttpLib()); // 注入API实例
        final String tag = "tag";
        final String activityName = this.getClass().getName();

        getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(android.app.Activity activity) {
                if (isActivityEquals(activityName, activity)) {
                    Log.v(tag, "onActivityStopped");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.v(tag, "onActivityStarted");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.v(tag, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.v(tag, "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.v(tag, "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.v(tag, "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.v(tag, "onActivityCreated");
            }
        });
    }

    public boolean isActivityEquals(String from, Activity to) {
        return from.equals(to.getClass().getName());
    }
}
