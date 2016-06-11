package kale.http.example;

import kale.http.skin.annotation.ApiInterface;
import kale.http.skin.annotation.HttpGet;
import kale.http.example.client.UrlConnectionRequest;
import kale.http.example.model.Root;

/**
 * @author Kale
 * @date 2016/6/11
 */
@ApiInterface
public interface UrlConnectionApi {

    @HttpGet(value = "/random/data/Android/1" )
    UrlConnectionRequest.Call<Root> testUrlConnectionGet(String test);
}
