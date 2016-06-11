package kale.http.skin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Jack Tony
 * @date 2015/8/20
 */
public class RequestSkin {

    public static final String PACKAGE_NAME = "kale.http.skin";

    public static final String CLASS_NAME = "HttpRequestEntity";

    public static <T> T create(HttpRequest httpRequest) {
        Object httpRequestEntity = null;
        try {
            Class httpRequestEntityClz = Class.forName(PACKAGE_NAME + "." + CLASS_NAME);
            Constructor<T> con = httpRequestEntityClz.getConstructor(HttpRequest.class);
            httpRequestEntity = con.newInstance(httpRequest);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) httpRequestEntity;
    }
}
