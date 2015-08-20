package kale.net.http.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import kale.net.http.impl.HttpRequest;
import kale.net.http.processor.HttpProcessor;

/**
 * @author Jack Tony
 * @date 2015/8/20
 */
public class HttpReqAdapter {

    public static <T> T create(HttpRequest httpRequest) {
        Object httpRequestEntity = null;
        try {
            Class<T> httpRequestEntityCls = (Class<T>) Class.forName(HttpProcessor.PACKAGE_NAME + "." + HttpProcessor.CLASS_NAME);
            Constructor con = httpRequestEntityCls.getConstructor(HttpRequest.class);
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
