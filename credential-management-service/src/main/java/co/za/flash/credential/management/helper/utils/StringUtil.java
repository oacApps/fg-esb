package co.za.flash.credential.management.helper.utils;

import java.util.*;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String getDomainFromUserName(String userNameWithDomain) {
        String separator = "/";
        if (StringUtil.isNullOrBlank(userNameWithDomain))
            return "";
        if (!userNameWithDomain.contains(separator))
            return "";
        int index = userNameWithDomain.indexOf(separator);
        return userNameWithDomain.substring(0, index);
    }

    public static String joinUrl(String baseUrl, String path, String endPoint) {
        StringBuilder builder = new StringBuilder();
        // 1. base url cannot be blank
        if (isNullOrBlank(baseUrl))
            return builder.toString();

        // 2. append base url and make sure all blank spaces are removed, be lowercase
        String tempUrl = baseUrl.replace(" ", "");
        // check if it has http:// or https
        if (!tempUrl.toLowerCase().startsWith("http://") && !tempUrl.toLowerCase().startsWith("https://")) {
            builder.append("https://");
        }
        // if it ends with "/"
        if (tempUrl.endsWith("/")) {
            tempUrl = tempUrl.substring(0, tempUrl.length());
        }
        builder.append(tempUrl);

        // 3. check if path is there
        if (!isNullOrBlank(path)) {
            tempUrl = path;
            // check if it starts with /
            if (tempUrl.startsWith("/")) {
                tempUrl = tempUrl.substring(1);
            }
            if (tempUrl.endsWith("/")) {
                tempUrl = tempUrl.substring(0, tempUrl.length());
            }
            // append it
            builder.append("/");
            builder.append(tempUrl);
        }

        // 4. check if endpoint exists
        if (!isNullOrBlank(endPoint)) {
            tempUrl = endPoint;
            // check if it starts with /
            if (tempUrl.startsWith("/")) {
                tempUrl = tempUrl.substring(1);
            }
            if (tempUrl.endsWith("/")) {
                tempUrl = tempUrl.substring(0, tempUrl.length());
            }
            // append it
            builder.append("/");
            builder.append(tempUrl);
        }

        return builder.toString();
    }

    private static final Set<String> JSON_MASKABLE_KEYS = new HashSet<>(Arrays.asList(
            "email",
            "emails",
            "phone",
            "pin",
            "password",
            "phonenumber",
            "moneys"));
    private static final Set<String> XML_MASKABLE_KEYS = new HashSet<>(Arrays.asList(
            "ser:credential"));

    private static final String MASKING_VALUE = "****";

    private static boolean isJsonBody(String bodyString) {
        return bodyString.startsWith("{") && bodyString.endsWith("}");
    }

    private static boolean isXmlBody(String bodyString) {
        return bodyString.startsWith("<") && bodyString.endsWith(">");
    }

    public static String maskPersonalInfo(String originalString) {
        if (!isNullOrBlank(originalString)) {
            if (isJsonBody(originalString))
                return JsonParseUtil.maskPersonalInfo(originalString, JSON_MASKABLE_KEYS, MASKING_VALUE);
            else if (isXmlBody(originalString))
                return SoapEnvelopeParser.maskPersonalInfo(originalString, XML_MASKABLE_KEYS, MASKING_VALUE);
        }
        return originalString;
    }

    public static boolean isValidEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}
