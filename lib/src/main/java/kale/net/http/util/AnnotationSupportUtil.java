package kale.net.http.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jack Tony
 * @date 2015/9/26
 */
public class AnnotationSupportUtil {

    private List<Class> annList = new ArrayList<>();
    
    /**
     * @param annotationCls example: MyAnnotation.class
     */
    public AnnotationSupportUtil support(Class<?> annotationCls) {
        annList.add(annotationCls);
        return this;
    }
    
    public Set<String> get() {
        Set<String> annotationSet = new LinkedHashSet<>();
        for (Class aClass : annList) {
            annotationSet.add(aClass.getCanonicalName());
        }
        return annotationSet;
    }
}
