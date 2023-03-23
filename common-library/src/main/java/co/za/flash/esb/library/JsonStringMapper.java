package co.za.flash.esb.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStringMapper {

    public static String toJsonString(Object object){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Object toObject(String jsonString){
        ObjectMapper mapper = new ObjectMapper();
        Object obj = new Object();
        try {
            obj = mapper.readValue(jsonString, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
