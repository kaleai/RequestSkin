package kale.http.example.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Kale
 * @date 2016/6/11
 */
public class Root {

    @SerializedName("error")
    public boolean error;

    @SerializedName("results")
    public List<Result> result;
}
