package kale.http.example;

import kale.http.skin.annotation.ApiInterface;
import kale.http.skin.annotation.HttpGet;
import kale.http.example.model.Root;
import rx.Observable;

/**
 * @author Kale
 * @date 2016/6/11
 */
@ApiInterface
public interface MockHttpApi {

    @HttpGet(value = "/random/data/Android/1" )
    Observable<Root> testMockGet();
}
