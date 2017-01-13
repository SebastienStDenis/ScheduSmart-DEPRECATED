package com.uwschedulebuilder.bootstrap;

import com.google.gson.Gson;
import spark.ResponseTransformer;
 
// JsonTransformer is used to convert data to JSON
public class JsonTransformer implements ResponseTransformer { 
    private Gson gson = new Gson();
 
    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}