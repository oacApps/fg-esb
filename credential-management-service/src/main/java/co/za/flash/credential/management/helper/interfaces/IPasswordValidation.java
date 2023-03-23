package co.za.flash.credential.management.helper.interfaces;

import java.util.List;

public interface IPasswordValidation {
    public String getErrorMessage();
    public List<String> getInvalidExamples();
    public boolean validate(List<Object> inputs);
}
