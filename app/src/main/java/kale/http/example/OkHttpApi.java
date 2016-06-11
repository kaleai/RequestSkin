package kale.http.example;

import kale.http.skin.annotation.ApiInterface;
import kale.http.skin.annotation.HttpGet;
import okhttp3.Call;

/**
 * @author Kale
 * @date 2016/6/11
 */
@ApiInterface
public interface OkHttpApi {

    @HttpGet(value = "/random/data/Android/1" )
    Call testOkHttpGet(String name);
}
