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

    String TEST_API = "search/";

    @HttpGet("test/")
    Observable get011();

    @HttpGet(TEST_API)
    Observable<MainActivity> get021();

    @HttpGet(value = TEST_API)
    Observable<MainActivity> get031(String test_api00, String color, String hehe);

    @HttpGet(value = "just/tttt" + "?user=123&name=abc")
    Observable<StringBuilder> get041(String test_api00, String rgb_color, String test);

    @HttpPost(TEST_API)
    Observable post011();

    @HttpPost(value = TEST_API)
    Observable<String[]> post021();

    @HttpPost(TEST_API + "?user=aaaa3&name=kale")
    Observable<MainActivity> post031();
    
    @HttpPost("search?user=aaaa3&name=kale")
    Observable post041(String create_time, String user_name);

  /*  @HttpPost("search?site=phoneNumber")
    Observable<String> testApi(String phone_number);*/

    String TEST_SIMPLE = "blog/list/by_club_id/"; // api url

    @HttpGet(value = TEST_SIMPLE + "?club_id=54aa79d9a3101a0f75731c62&limit=0")
    Observable<MyHttpLib> testApi01(String start);
    
}
