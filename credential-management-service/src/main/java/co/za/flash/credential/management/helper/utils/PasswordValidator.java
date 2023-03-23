package co.za.flash.credential.management.helper.utils;

import co.za.flash.credential.management.helper.enums.PasswordValidationRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordValidator {
    public static PasswordValidationRules validateAllRules(List<Object> inputs) {
        ArrayList<PasswordValidationRules> failed = new ArrayList<>();
        // validate with all cases
        Arrays.stream(PasswordValidationRules.values()).forEach(rule-> {
            if (!rule.validate(inputs))
                failed.add(rule);
        });
        if (failed.isEmpty())
            return null;
        return failed.get(0);
    }

    public static PasswordValidationRules validateWithExclusions(List<Object> inputs, List<Integer> excludeCodes) {
        if (excludeCodes == null || excludeCodes.size() == 0)
            return validateAllRules(inputs);

        ArrayList<PasswordValidationRules> failed = new ArrayList<>();
        // validate with all cases
        Arrays.stream(PasswordValidationRules.values()).forEach(rule-> {
            if (!excludeCodes.contains(rule.code)) {
                if (!rule.validate(inputs))
                    failed.add(rule);
            }
        });
        if (failed.isEmpty())
            return null;
        return failed.get(0);
    }
}
