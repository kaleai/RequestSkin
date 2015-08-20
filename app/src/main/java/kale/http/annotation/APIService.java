package kale.http.annotation;


import kale.net.http.annotation.ApiInterface;
import kale.net.http.annotation.HttpGet;
import kale.net.http.annotation.HttpPost;
import rx.Observable;

/**
 * @author Jack Tony
 * @date 2015/8/16
 */
@ApiInterface
public interface APIService {

    String TEST_API = "http://image.baidu.com";

    @HttpGet(url = "test/")
    Observable get011();

    @HttpGet(url = TEST_API, model = MainActivity.class)
    Observable get021();

    @HttpGet(url = TEST_API, model = MainActivity.class)
    Observable<MainActivity> get031(String test_api00, String dd_color, String atdsfslass);

    @HttpGet(url = "just/tttt" + "?user=123&name=abc", model = MainActivity.class)
    Observable<MainActivity> get041(String test_api00, String dd_color, String dfdf2);

    @HttpPost(url = TEST_API)
    Observable post011();

    @HttpPost(url = TEST_API, model = MainActivity.class)
    Observable post021();

    @HttpPost(url = TEST_API + "?user=aaaa3&name=kale")
    Observable post031();

    @HttpPost(url = TEST_API + "?user=aaaa3&name=kale")
    Observable post041(String create_time, String user_name);

    @HttpPost(url = "/napi/create_user/by_auto?site=phoneNumber")
    Observable<String> testApi(String phone_number);

}
