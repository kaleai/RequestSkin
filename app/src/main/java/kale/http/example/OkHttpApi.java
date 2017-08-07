package kale.http.example;

import java.io.File;

import kale.http.skin.annotation.ApiInterface;
import kale.http.skin.annotation.HttpGet;
import kale.http.skin.annotation.HttpPost;
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
