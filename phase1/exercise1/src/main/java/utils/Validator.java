package utils;

public class Validator {
    private static final String MOBILE_REGEX = "^[0-9]{10}$";
    public static boolean isValidMobile(String number) {
        return number != null && number.matches(MOBILE_REGEX);
    }
}
