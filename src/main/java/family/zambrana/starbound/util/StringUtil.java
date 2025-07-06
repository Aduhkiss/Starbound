package family.zambrana.starbound.util;

public class StringUtil {
    public static String removeLeadingZeros(String input) {
        // Remove leading zeros using regular expression
        return input.replaceFirst("^0+(?!$)", "");
    }
    public static String combine(String[] arr, int startPos) {
        StringBuilder str = new StringBuilder();

        for(int i = startPos; i < arr.length; ++i) {
            str = str.append(arr[i] + " ");
        }
        return str.toString();
    }
}
