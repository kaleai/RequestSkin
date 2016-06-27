package kale.http.example;

import java.util.List;

import kale.http.example.model.Result;
import kale.http.example.model.Root;
import kale.http.skin.annotation.ApiInterface;
import kale.http.skin.annotation.HttpGet;
import rx.Observable;

/**
 * @author Kale
 * @date 2016/6/11
 */
@ApiInterface
public interface MockHttpApi {

    @HttpGet(value = "/random/data/Android/1" )
    Observable<Root> testMockGet();

    @HttpGet(value = "/random/data/Android/5" )
    Observable<List<Result>> testMockGetList();

}
