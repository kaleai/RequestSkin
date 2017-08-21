package kale.http.skin;

/**
 * Created by YLM on 2017/8/16.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import kale.http.skin.annotation.HttpGet;
import kale.http.skin.annotation.HttpPost;
import kale.http.skin.annotation.Multipart;
import kale.http.skin.annotation.parameter.Field;
import kale.http.skin.annotation.parameter.FieldMap;
import kale.http.skin.annotation.parameter.Part;
import kale.http.skin.annotation.parameter.PartMap;
import kale.http.skin.annotation.parameter.Path;
import kale.http.skin.annotation.parameter.Query;
import kale.http.skin.annotation.parameter.QueryMap;



/**
 * Created by YLM on 2017/8/15.
 */

public class ServiceMethod {

    private ParameterHandler handler;

    public ServiceMethod(ParameterHandler parameterHandler){
        this.handler = parameterHandler;
    }

    public String toResult(){
        return handler.apply();
    }

    static final class Builder{

        private String relativeUrl;

        private boolean isMultipart;

        private boolean isGet;

        private boolean isPost;

        private String httpMethod;

        private ExecutableElement mMethod;

        private ProcessingEnvironment processingEnvironment;

        private ParameterHandler handlers;


        public Builder(ExecutableElement method, ProcessingEnvironment processingEnvironment){
            this.mMethod = method;
            this.processingEnvironment = processingEnvironment;
        }

        public ServiceMethod build(){
            parseMethodAnnotation(mMethod);
            handlers = parseParameters(mMethod.getParameters());
            return new ServiceMethod(handlers);
        }

        private void parseMethodAnnotation(ExecutableElement method){
            if (method.getAnnotation(Multipart.class) != null){
                isMultipart = true;
            }
            if (method.getAnnotation(HttpGet.class) != null){
                if (isPost){
                    //error
                }
                isGet = true;
                parseHttpMethodAndPath("GET",method.getAnnotation(HttpGet.class).value(),false);
            }else if (method.getAnnotation(HttpPost.class) != null){
                if (isGet){
                    //error
                }
                isPost = true;
                parseHttpMethodAndPath("POST",method.getAnnotation(HttpPost.class).value(),true);
            }

        }

        private ParameterHandler parseParameters(List<? extends VariableElement> parameters){
            List<Integer> parameterTypes = new ArrayList<>();
            for (VariableElement ve : parameters){
                List<? extends AnnotationMirror> annotationMirrors =ve.getAnnotationMirrors();
                if (annotationMirrors.size()>=2){
                    //error : parameter only can have one annomatation
                }
                int currentType = PARAMETER_TYPE.DEFAULT;
                for (AnnotationMirror am : annotationMirrors){
                    currentType =  parseParameterAnnotation(am);
                }
                parameterTypes.add(currentType);
            }
            return chooseHandler(parameterTypes,parameters);

        }

        private int parseParameterAnnotation(AnnotationMirror annotationMirror){
            if (annotationMirror.getAnnotationType().toString().equals(Part.class.getCanonicalName())){
                if (isGet){
                    //error
                }
                if (!isMultipart){
                    //error
                }
                return PARAMETER_TYPE.PART;
            }else if (annotationMirror.getAnnotationType().toString().equals(PartMap.class.getCanonicalName())){

            }else if (annotationMirror.getAnnotationType().toString().equals(Field.class.getCanonicalName())){

            }else if (annotationMirror.getAnnotationType().toString().equals(FieldMap.class.getCanonicalName())){

            }else if (annotationMirror.getAnnotationType().toString().equals(Query.class.getCanonicalName())){

            }else if (annotationMirror.getAnnotationType().toString().equals(QueryMap.class.getCanonicalName())){

            }else if (annotationMirror.getAnnotationType().toString().equals(Path.class.getCanonicalName())){

            }

            return PARAMETER_TYPE.DEFAULT;
        }

        private ParameterHandler chooseHandler(List<Integer> parameterTypes,List<? extends VariableElement> parameters){
            if (parameterTypes.contains(PARAMETER_TYPE.PART)){
                return new ParameterHandler.Parts(mMethod,parameters,"doPostMultipart",relativeUrl);
            }else {
                return new ParameterHandler.Default();
            }
        }


        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody){
            this.httpMethod = httpMethod;
            if (value.isEmpty()){
                return;
            }
            this.relativeUrl = value;
        }



        public @interface PARAMETER_TYPE{
            int DEFAULT = 0;
            int PART = 1;

        }
    }

}
