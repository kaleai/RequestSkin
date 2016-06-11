package kale.http.skin;


import java.util.Map;

/**
 * @author Jack Tony
 * @date 2015/8/4
 */
public interface HttpRequest {

    ////////////////////////////////// POST //////////////////////////////////

    <T> Object doPost(final String url, final Map<String, String> map, final Class<T> modelClass);

    ////////////////////////////////// GET //////////////////////////////////

    <T> Object doGet(final String url, final Class<T> modelClass);

}
