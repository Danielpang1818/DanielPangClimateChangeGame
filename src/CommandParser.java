public class CommandParser {
    public static boolean parseYesNo(String input) {
        input = input.trim().toLowerCase();
        return input.equals("yes");
    }

    public static boolean isValidYesNo(String input) {
        input = input.trim().toLowerCase();
        return input.equals("yes") || input.equals("no");
    }
}
