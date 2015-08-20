package kale.net.http.impl;


import java.util.HashMap;

/**
 * @author Jack Tony
 * @date 2015/8/4
 */
public interface HttpRequest {

    ////////////////////////////////// POST //////////////////////////////////

    <Model> Object doPost(String url);

    <Model> Object doPost(String url, Class<Model> modelClass);

    <Model> Object doPost(String url, HashMap<String, String> map);

    <Model> Object doPost(final String url, final HashMap<String, String> map,
            final Class<Model> modelClass);

    ////////////////////////////////// GET //////////////////////////////////

    <Model> Object doGet(String url);

    <Model> Object doGet(final String url, final Class<Model> modelClass);

}
