package co.za.flash.credential.management.helper.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Base64;
import java.util.Locale;
import java.util.Map;

public class JwtTokenUtil {
    // https://inthiraj1994.medium.com/signature-verification-using-jwks-endpoint-in-wso2-identity-server-5ba65c5de086
    public static final String domainClaimFieldKey = "http://wso2.org/claims/cred-man-domain";

    // use standard lib for validation
    private static final String jwtTokenHeaderKey = "X-JWT-Assertion";
    private static final String tokenSeparator = ".";
    public static Map<String, Object> getJWTClaims(Map<String, String> headers)
            throws JwtException {
        // 1. has header?
        String rawHeader = getJWTHeader(headers);
        if (StringUtil.isNullOrBlank(rawHeader))
            return null;
        // 2. parse data
        String[] chunks = rawHeader.split("\\.");
        try {
            if (chunks.length > 1) {
                byte[] decodedBytes = Base64.getMimeDecoder().decode(chunks[1]);
                String payload = new String(decodedBytes);
                JsonObject rootObj = JsonParser.parseString(payload).getAsJsonObject();
                return JsonParseUtil.getJsonAsMap(rootObj);
            } else {
                throw new JwtException("Invalid token body");
            }
        } catch (Throwable t) {
            throw new JwtException(t.getMessage());
        }
    }

    private static String getJWTHeader(Map<String, String> headers) {
        if (headers == null || headers.isEmpty() ||
                !headers.keySet().stream().anyMatch(item->
                        item.toLowerCase(Locale.ROOT).equals(jwtTokenHeaderKey.toLowerCase(Locale.ROOT))))
            return "";
        String header = headers.entrySet().stream().filter(entry->
                        entry.getKey().toLowerCase(Locale.ROOT).equals(jwtTokenHeaderKey.toLowerCase(Locale.ROOT))).
                findFirst().get().getValue();
        if (!header.contains(tokenSeparator))
            return "";
        return header;
    }
}
