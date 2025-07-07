package family.zambrana.starbound.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {

    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)([a-zA-Z]+)");

    public static long parseDurationToMillis(String input) {
        Matcher matcher = DURATION_PATTERN.matcher(input);
        long totalMillis = 0L;

        while (matcher.find()) {
            long amount = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();

            switch (unit) {
                case "y":
                    totalMillis += Duration.of(amount * 365, ChronoUnit.DAYS).toMillis(); break;
                case "mo":
                    totalMillis += Duration.of(amount * 30, ChronoUnit.DAYS).toMillis(); break;
                case "w":
                    totalMillis += Duration.of(amount * 7, ChronoUnit.DAYS).toMillis(); break;
                case "d":
                    totalMillis += Duration.of(amount, ChronoUnit.DAYS).toMillis(); break;
                case "h":
                    totalMillis += Duration.of(amount, ChronoUnit.HOURS).toMillis(); break;
                case "m":
                    totalMillis += Duration.of(amount, ChronoUnit.MINUTES).toMillis(); break;
                case "s":
                    totalMillis += Duration.of(amount, ChronoUnit.SECONDS).toMillis(); break;
                default:
                    throw new IllegalArgumentException("Invalid time unit: " + unit);
            }
        }

        return totalMillis;
    }
}