package kale.http.skin;


import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Jack Tony
 * @date 2015/8/4
 */
public interface HttpRequest {

    ////////////////////////////////// POST //////////////////////////////////

    <T> Object doPost(final String url, final Map<String, String> map, final Class<T> modelClass);

    <T> Object doPost(final String url, final Map<String, String> map, final Type type);

    ////////////////////////////////// GET //////////////////////////////////

    <T> Object doGet(final String url, final Class<T> modelClass);

    <T> Object doGet(final String url, final Type type);

}
