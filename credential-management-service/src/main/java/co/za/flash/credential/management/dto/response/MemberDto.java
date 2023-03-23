package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class MemberDto {
     private String display;
     private String value;

     private MemberDto(String display, String value) {
         this.display = display;
         this.value = value;
     }

     public static MemberDto parse(JsonObject rootObj) {
         String id = JsonParseUtil.parseString(rootObj, "display", "");
         String userName = JsonParseUtil.parseString(rootObj, "value", "");
         return new MemberDto(id, userName);
     }

     public boolean isValid() {
        return (!StringUtil.isNullOrBlank(display) && !StringUtil.isNullOrBlank(value));
    }
}
