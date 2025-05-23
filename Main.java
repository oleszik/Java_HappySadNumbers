package numbers;

import java.util.*;

public class Main {
    private static final Set<String> PROPERTIES = new TreeSet<>(Arrays.asList(
            "EVEN", "ODD", "BUZZ", "DUCK", "PALINDROMIC", "GAPFUL",
            "SPY", "SQUARE", "SUNNY", "JUMPING", "HAPPY", "SAD"
    ));

    private static final List<Set<String>> MUTUALLY_EXCLUSIVE = List.of(
            Set.of("EVEN", "ODD"),
            Set.of("DUCK", "SPY"),
            Set.of("SQUARE", "SUNNY"),
            Set.of("HAPPY", "SAD")
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printInstructions();

        while (true) {
            System.out.print("Enter a request: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                printInstructions();
                continue;
            }

            String[] parts = input.split("\\s+");

            try {
                long first = Long.parseLong(parts[0]);
                if (first == 0) {
                    System.out.println("Goodbye!");
                    break;
                }
                if (first < 0) {
                    System.out.println("The first parameter should be a natural number or zero.");
                    continue;
                }

                if (parts.length == 1) {
                    printProperties(first);
                } else if (parts.length >= 2) {
                    long count = Long.parseLong(parts[1]);
                    if (count <= 0) {
                        System.out.println("The second parameter should be a natural number.");
                        continue;
                    }

                    // Separate included and excluded properties
                    Set<String> includeProps = new LinkedHashSet<>();
                    Set<String> excludeProps = new LinkedHashSet<>();
                    List<String> invalidProps = new ArrayList<>();

                    for (int i = 2; i < parts.length; i++) {
                        String prop = parts[i].toUpperCase();
                        boolean isExcluded = prop.startsWith("-");
                        String propName = isExcluded ? prop.substring(1) : prop;

                        if (!PROPERTIES.contains(propName)) {
                            invalidProps.add(prop);
                        } else {
                            if (isExcluded) {
                                excludeProps.add(propName);
                            } else {
                                includeProps.add(propName);
                            }
                        }
                    }

                    if (!invalidProps.isEmpty()) {
                        if (invalidProps.size() == 1) {
                            System.out.printf("The property [%s] is wrong.%n", invalidProps.get(0));
                        } else {
                            System.out.printf("The properties %s are wrong.%n", invalidProps);
                        }
                        System.out.println("Available properties: " + PROPERTIES);
                        continue;
                    }

                    // Check mutually exclusive pairs (including direct opposites)
                    boolean conflict = false;

                    // Check standard mutually exclusive sets
                    for (Set<String> pair : MUTUALLY_EXCLUSIVE) {
                        if (includeProps.containsAll(pair)) {
                            System.out.printf("The request contains mutually exclusive properties: %s%n", pair);
                            System.out.println("There are no numbers with these properties.");
                            conflict = true;
                            break;
                        }
                        if (excludeProps.containsAll(pair)) {
                            System.out.printf("The request contains mutually exclusive properties: [%s, %s]%n",
                                    "-" + pair.iterator().next(), "-" + pair.stream().skip(1).findFirst().get());
                            System.out.println("There are no numbers with these properties.");
                            conflict = true;
                            break;
                        }
                    }
                    if (conflict) continue;

                    // Check direct opposites, e.g. "EVEN" and "-EVEN"
                    for (String prop : includeProps) {
                        if (excludeProps.contains(prop)) {
                            System.out.printf("The request contains mutually exclusive properties: [%s, -%s]%n", prop, prop);
                            System.out.println("There are no numbers with these properties.");
                            conflict = true;
                            break;
                        }
                    }
                    if (conflict) continue;

                    // Search numbers matching the request
                    long found = 0;
                    long number = first;

                    while (found < count) {
                        boolean matchesIncluded = true;
                        for (String prop : includeProps) {
                            if (!hasProperty(number, prop)) {
                                matchesIncluded = false;
                                break;
                            }
                        }
                        boolean matchesExcluded = true;
                        for (String prop : excludeProps) {
                            if (hasProperty(number, prop)) {
                                matchesExcluded = false;
                                break;
                            }
                        }

                        if (matchesIncluded && matchesExcluded) {
                            System.out.println(describeNumber(number));
                            found++;
                        }
                        number++;
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("The first parameter should be a natural number or zero.");
            }
        }
    }

    private static void printInstructions() {
        System.out.println("Welcome to Amazing Numbers!\n");
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be processed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.\n");
    }

    private static void printProperties(long number) {
        System.out.println("\nProperties of " + String.format("%,d", number));
        System.out.printf("        buzz: %b%n", isBuzz(number));
        System.out.printf("        duck: %b%n", isDuck(number));
        System.out.printf(" palindromic: %b%n", isPalindromic(number));
        System.out.printf("      gapful: %b%n", isGapful(number));
        System.out.printf("         spy: %b%n", isSpy(number));
        System.out.printf("      square: %b%n", isSquare(number));
        System.out.printf("       sunny: %b%n", isSunny(number));
        System.out.printf("     jumping: %b%n", isJumping(number));
        System.out.printf("       happy: %b%n", isHappy(number));
        System.out.printf("         sad: %b%n", isSad(number));
        System.out.printf("        even: %b%n", number % 2 == 0);
        System.out.printf("         odd: %b%n", number % 2 != 0);
        System.out.println();
    }

    private static String describeNumber(long number) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%,d", number)).append(" is");

        if (number % 2 == 0) sb.append(" even");
        if (number % 2 != 0) sb.append(" odd");
        if (isBuzz(number)) sb.append(", buzz");
        if (isDuck(number)) sb.append(", duck");
        if (isPalindromic(number)) sb.append(", palindromic");
        if (isGapful(number)) sb.append(", gapful");
        if (isSpy(number)) sb.append(", spy");
        if (isSquare(number)) sb.append(", square");
        if (isSunny(number)) sb.append(", sunny");
        if (isJumping(number)) sb.append(", jumping");
        if (isHappy(number)) sb.append(", happy");
        if (isSad(number)) sb.append(", sad");

        return sb.toString();
    }

    private static boolean hasProperty(long number, String property) {
        return switch (property) {
            case "BUZZ" -> isBuzz(number);
            case "DUCK" -> isDuck(number);
            case "PALINDROMIC" -> isPalindromic(number);
            case "GAPFUL" -> isGapful(number);
            case "SPY" -> isSpy(number);
            case "SQUARE" -> isSquare(number);
            case "SUNNY" -> isSunny(number);
            case "JUMPING" -> isJumping(number);
            case "HAPPY" -> isHappy(number);
            case "SAD" -> isSad(number);
            case "EVEN" -> number % 2 == 0;
            case "ODD" -> number % 2 != 0;
            default -> false;
        };
    }

    private static boolean isBuzz(long number) {
        return number % 7 == 0 || number % 10 == 7;
    }

    private static boolean isDuck(long number) {
        return String.valueOf(number).contains("0");
    }

    private static boolean isPalindromic(long number) {
        String numStr = Long.toString(number);
        return numStr.equals(new StringBuilder(numStr).reverse().toString());
    }

    private static boolean isGapful(long number) {
        String numStr = Long.toString(number);
        if (numStr.length() < 3) return false;
        String concat = "" + numStr.charAt(0) + numStr.charAt(numStr.length() - 1);
        long concatNum = Long.parseLong(concat);
        return number % concatNum == 0;
    }

    private static boolean isSpy(long number) {
        String numStr = Long.toString(number);
        int sum = 0;
        int product = 1;
        for (char digit : numStr.toCharArray()) {
            int d = digit - '0';
            sum += d;
            product *= d;
        }
        return sum == product;
    }

    private static boolean isSquare(long number) {
        long sqrt = (long) Math.sqrt(number);
        return sqrt * sqrt == number;
    }

    private static boolean isSunny(long number) {
        return isSquare(number + 1);
    }

    private static boolean isJumping(long number) {
        String numStr = Long.toString(number);
        if (numStr.length() == 1) return true;
        for (int i = 1; i < numStr.length(); i++) {
            int diff = Math.abs(numStr.charAt(i) - numStr.charAt(i - 1));
            if (diff != 1) return false;
        }
        return true;
    }

    private static boolean isHappy(long number) {
        Set<Long> seen = new HashSet<>();
        long n = number;
        while (n != 1 && !seen.contains(n)) {
            seen.add(n);
            n = sumOfSquaresOfDigits(n);
        }
        return n == 1;
    }

    private static boolean isSad(long number) {
        return !isHappy(number);
    }

    private static long sumOfSquaresOfDigits(long n) {
        long sum = 0;
        while (n > 0) {
            long digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }
        return sum;
    }
}
