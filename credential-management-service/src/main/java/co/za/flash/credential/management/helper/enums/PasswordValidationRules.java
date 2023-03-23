package co.za.flash.credential.management.helper.enums;

import co.za.flash.credential.management.helper.interfaces.IPasswordValidation;
import co.za.flash.credential.management.helper.utils.StringUtil;

import java.util.*;
import java.util.regex.Pattern;

public enum PasswordValidationRules implements IPasswordValidation {
    AllowedPatterns(0, "AllowedPatterns") {
        // allowed: digits with at least 1 capital letter and small letters, and at least 1 special character
        @Override
        public String getErrorMessage() {
            return "Password must contain at least 1 digit, 1 capital, 1 lower case and 1 special char";
        }

        @Override
        public List<String> getInvalidExamples() {
            return Arrays.asList("asdcDf1", "asdcDf%", "6d1%", "dfGh%");
        }

        private String getRegexPattern() {
            // 1 digit, 1 capital, 1 lower case and 1 special char
            return "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{4,}$";
        }

        @Override
        public boolean validate(List<Object> inputs) {
            String input = "";
            if (inputs == null || inputs.size() <= 0)
                return false;
            input = inputs.get(0).toString();
            return Pattern.compile(getRegexPattern()).matcher(input).matches();
        }
    },
    LengthLimitation(1, "LengthLimitation") {
        private static final int minLength = 6;
        private static final int maxLength = 16;
        @Override
        public String getErrorMessage() {
            return String.format("Password length must be between %d and %d inclusively.", minLength, maxLength);
        }

        @Override
        public List<String> getInvalidExamples() {
            return Arrays.asList("dG1%", "dG1%dG1%dG1%dG1%2");
        }

        @Override
        public boolean validate(List<Object> inputs) {
            String input = "";
            if (inputs == null || inputs.size() <= 0)
                return false;
            input = inputs.get(0).toString();
            return input.length() >= minLength && input.length() <= maxLength;
        }
    },
    RepetitionWithUsername(2, "RepetitionWithUsername") {
        @Override
        public String getErrorMessage() {
            return "Password cannot contain your username.";
        }

        @Override
        public List<String> getInvalidExamples() {
            return Arrays.asList("Flash as username, Flash123! as password");
        }

        @Override
        public boolean validate(List<Object> inputs) {
            String inputUsername = "";
            String inputPassword = "";
            if (inputs == null || inputs.size() <= 1)
                return false;
            inputUsername = inputs.get(1).toString().toLowerCase(Locale.ROOT);
            // password is always the first input!
            inputPassword = inputs.get(0).toString().toLowerCase(Locale.ROOT);
            return !inputPassword.contains(inputUsername);
        }
    },
    RepetitionPatterns(3, "RepetitionPatterns") {
        @Override
        public String getErrorMessage() {
            return "Password contains duplicated patterns.";
        }

        @Override
        public List<String> getInvalidExamples() {
            return Arrays.asList("dF1!dF1!");
        }

        private String getRegexPattern() {
            return "^(.+?)(?:\\1)+$";
        }

        @Override
        public boolean validate(List<Object> inputs) {
            String input = "";
            if (inputs == null || inputs.size() <= 0)
                return false;
            input = inputs.get(0).toString();
            return !Pattern.compile(getRegexPattern()).matcher(input).matches();
        }
    },
    RepetitionChars(4, "RepetitionChars") {
        private int maxDuplicateLength = 3;
        @Override
        public String getErrorMessage() {
            return "Password contains three or more consecutive identical letters.";
        }

        @Override
        public List<String> getInvalidExamples() {
            return Arrays.asList("aaab1C!", "abC111#");
        }

        @Override
        public boolean validate(List<Object> inputs) {
            String input = "";
            if (inputs == null || inputs.size() <= 0)
                return false;
            input = inputs.get(0).toString();
            if (StringUtil.isNullOrBlank(input))
                return false;
            int counter = 0;
            for (int i = 0; i < input.length() - 1; i++) {
                char currentChar = input.charAt(i);
                char nextChar = input.charAt(i + 1);
                if (currentChar == nextChar) {
                    counter ++;
                } else {
                    counter = 0;
                }
                if (counter >= maxDuplicateLength - 1) {
                    return false;
                }
            }
            return true;
        }
    },
    WordExclusion(5, "WordExclusion") {
        private List<String> exclusionList = Arrays.asList(
                "password",
                "flash",
                "trader",
                "pin",
                "pass"
        );
        @Override
        public String getErrorMessage() {
            return "Password contains easy to guess words.";
        }

        @Override
        public List<String> getInvalidExamples() {
            return Arrays.asList("Password123!");
        }

        @Override
        public boolean validate(List<Object> inputs) {
            if (inputs == null || inputs.size() <= 0)
                return false;
            String input = inputs.get(0).toString().toLowerCase(Locale.ROOT);
            return !exclusionList.stream().anyMatch(item ->
                    input.contains(item.toLowerCase(Locale.ROOT)));
        }
    };

    public final String description;
    public final int code;

    private PasswordValidationRules(int code, String description) {
        this.code = code;
        if (!StringUtil.isNullOrBlank(description)) {
            this.description = description;
        } else {
            this.description = "";
        }
    }

    public static PasswordValidationRules getInstance(int code) {
        if (code == PasswordValidationRules.AllowedPatterns.code) {
            return AllowedPatterns;
        }
        if (code == PasswordValidationRules.LengthLimitation.code) {
            return LengthLimitation;
        }
        if (code == PasswordValidationRules.RepetitionWithUsername.code) {
            return RepetitionWithUsername;
        }
        if (code == PasswordValidationRules.RepetitionPatterns.code) {
            return RepetitionPatterns;
        }
        if (code == PasswordValidationRules.RepetitionChars.code) {
            return RepetitionChars;
        }
        if (code == PasswordValidationRules.WordExclusion.code) {
            return WordExclusion;
        }
        return null;
    }

    public static PasswordValidationRules getInstance(String description) {
        if (description.toLowerCase(Locale.ROOT).equals(PasswordValidationRules.AllowedPatterns.description.toLowerCase(Locale.ROOT))) {
            return AllowedPatterns;
        }
        if (description.toLowerCase(Locale.ROOT).equals(PasswordValidationRules.LengthLimitation.description.toLowerCase(Locale.ROOT))) {
            return LengthLimitation;
        }
        if (description.toLowerCase(Locale.ROOT).equals(PasswordValidationRules.RepetitionWithUsername.description.toLowerCase(Locale.ROOT))) {
            return RepetitionWithUsername;
        }
        if (description.toLowerCase(Locale.ROOT).equals(PasswordValidationRules.RepetitionPatterns.description.toLowerCase(Locale.ROOT))) {
            return RepetitionPatterns;
        }
        if (description.toLowerCase(Locale.ROOT).equals(PasswordValidationRules.RepetitionChars.description.toLowerCase(Locale.ROOT))) {
            return RepetitionChars;
        }
        if (description.toLowerCase(Locale.ROOT).equals(PasswordValidationRules.WordExclusion.description.toLowerCase(Locale.ROOT))) {
            return WordExclusion;
        }
        return null;
    }

    public static List<String> getInvalidExamples(int code) {
        PasswordValidationRules rule = getInstance(code);
        if (rule == null)
            return Collections.emptyList();
        return rule.getInvalidExamples();
    }
}
