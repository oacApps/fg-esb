package co.za.flash.credential.management.helper.utils;

import com.google.gson.*;

import java.util.*;

public class JsonParseUtil {
    public static String maskPersonalInfo(String originalString, Set<String> maskKeys, String maskingValue) {
        try {
            JsonElement rootEle = JsonParser.parseString(originalString);
            if (rootEle != null && !rootEle.isJsonNull() && rootEle.isJsonObject()) {
                JsonObject rootObj = rootEle.getAsJsonObject();
                JsonObject maskedObj = maskingForJsonObject(maskKeys, rootObj, maskingValue);
                return maskedObj.toString();
            } else if (rootEle != null && !rootEle.isJsonNull() && rootEle.isJsonArray()) {
                JsonArray rootArr = rootEle.getAsJsonArray();
                return maskingForArray(maskKeys, rootArr, maskingValue).toString();
            }
        } catch (Exception e) {
            // to do - Error while masking data
            e.getStackTrace();
        }
        return originalString;
    }

    public static Map<String, Object> getJsonAsMap(JsonObject jsonobj){
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keys = jsonobj.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JsonArray) {
                value = getJsonListAsMap((JsonArray) value);
            } else if (value instanceof JsonObject) {
                value = getJsonAsMap((JsonObject) value);
            } else {
                value = ((JsonElement) value).getAsString();
            }
            map.put(key, value);
        }   return map;
    }
    public static List<Object> getJsonListAsMap(JsonArray array){
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JsonArray) {
                value = getJsonListAsMap((JsonArray) value);
            }
            else if (value instanceof JsonObject) {
                value = getJsonAsMap((JsonObject) value);
            }
            list.add(value);
        }   return list;
    }

    private static boolean isValidSet(Set<String> set) {
        return set != null && !set.isEmpty();
    }

    private static boolean isKnownPrimitiveWrapperModel(Object obj) {
        return obj == null || obj instanceof String || obj instanceof Integer || obj instanceof Long
                || obj instanceof Double;
    }

    private static JsonObject maskingForJsonObject(Set<String> maskableKeys, JsonObject input, String maskingValue) {
        if (!isValidSet(maskableKeys) || input == null || input.isJsonNull()) {
            return input;
        }

        // to find the fields with mask value, parse in each layer and replace
        input.entrySet().stream().forEach(entry -> {
            JsonElement child = entry.getValue();
            String key = entry.getKey();
            if (child != null && !child.isJsonNull()) {
                if (child.isJsonObject()) {
                    JsonObject node = child.getAsJsonObject();
                    // parse it and replace the node
                    child = maskingForJsonObject(maskableKeys, node, maskingValue);
                } else if (child.isJsonArray()) {
                    JsonArray node = child.getAsJsonArray();
                    // parse it and replace the node
                    child = maskingForArray(maskableKeys, node, maskingValue);
                } else if (child.isJsonPrimitive() && maskableKeys.contains(key.toLowerCase())) {
                    child = new JsonPrimitive(maskingValue);
                }
            }
            entry.setValue(child);
        });
        return input;
    }

    @SuppressWarnings("unchecked")
    private static JsonArray maskingForArray(Set<String> maskableKeys,
                                             JsonArray jsonArr, String maskingValue) {
        if (jsonArr == null || jsonArr.isJsonNull() || !jsonArr.isJsonArray())
            return jsonArr;
        for (int i = 0; i < jsonArr.size(); i++) {
            JsonElement element = jsonArr.get(i);
            if (element != null && !element.isJsonNull()) {
                if (element.isJsonObject()) {
                    JsonObject node = element.getAsJsonObject();
                    jsonArr.set(i, maskingForJsonObject(maskableKeys, node, maskingValue));
                } else if (element.isJsonArray()) {
                    JsonArray node = element.getAsJsonArray();
                    jsonArr.set(i, maskingForArray(maskableKeys, node, maskingValue));
                }
            }
        }
        return jsonArr;
    }

    public static boolean parseBoolean(JsonObject root, String keySensitive, boolean defaultValue) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonPrimitive()) {
                return element.getAsBoolean();
            }
        }
        return defaultValue;
    }

    public static int parseInt(JsonObject root, String keySensitive, int defaultValue) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonPrimitive()) {
                return element.getAsInt();
            }
        }
        return defaultValue;
    }

    public static long parseInt(JsonObject root, String keySensitive, long defaultValue) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonPrimitive()) {
                return element.getAsLong();
            }
        }
        return defaultValue;
    }

    public static double parseDouble(JsonObject root, String keySensitive, double defaultValue) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonPrimitive()) {
                return element.getAsDouble();
            }
        }
        return defaultValue;
    }

    public static float parseFloat(JsonObject root, String keySensitive, float defaultValue) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonPrimitive()) {
                return element.getAsFloat();
            }
        }
        return defaultValue;
    }

    public static String parseString(JsonObject root, String keySensitive, String defaultValue) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonPrimitive()) {
                return element.getAsString();
            }
        }
        return defaultValue;
    }

    public static JsonObject parseChild(JsonObject root, String keySensitive) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonObject()) {
                return element.getAsJsonObject();
            }
        }
        return null;
    }

    public static JsonArray parseChildList(JsonObject root, String keySensitive) throws JsonParseException {
        if (root != null && !root.isJsonNull() && root.has(keySensitive)) {
            JsonElement element = root.get(keySensitive);
            if (element != null && !element.isJsonNull() && element.isJsonArray()) {
                return element.getAsJsonArray();
            }
        }
        return null;
    }
}
