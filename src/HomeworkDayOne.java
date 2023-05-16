import java.util.*;
import java.util.stream.IntStream;

public class HomeworkDayOne {

    private static String[][] table;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (table == null) {
            resetTable(scanner);
        }
        while (true) {
            System.out.println("Enter an input (search|edit|print|reset|exit):");
            String input = scanner.nextLine();
            switch (input) {
                case "search" -> search(scanner);
                case "edit" -> edit(scanner);
                case "print" -> print();
                case "reset" -> resetTable(scanner);
                case "exit" -> System.exit(0);
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private static void resetTable(Scanner scanner) {
        try {
            System.out.print("Enter table dimensions (rows x columns ex: 3x3): ");
            String[] dimensions = scanner.nextLine().split("x");
            int rows = Integer.parseInt(dimensions[0]);
            int columns = Integer.parseInt(dimensions[1]);
            table = new String[rows][columns];
            for (int i = 0; i < rows; i++) {
                table[i] = Arrays.stream(new String[columns])
                        .map(j -> String.format("%3s", getRandomString(3)))
                        .toArray(String[]::new);
            }
            System.out.println("Table has been created/reset.");
        } catch (Exception e) {
            System.out.println("Invalid input format");
        }
    }

    private static String getRandomString(int length) {
        final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=[]{}|;':\",./<>?";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private static void search(Scanner scanner) {
        List<int[]> indices = new ArrayList<>();
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (String.valueOf(table[i][j]).contains(searchTerm)) {
                    count++;
                    indices.add(new int[]{i,j});
                }
            }
        }
        if (count == 0) {
            System.out.println("Search term not found.");
        } else {
            for (int[] index: indices) {
                long occurrenceCount;
                if(searchTerm.length() == 1) {
                    occurrenceCount = Arrays.stream(table[index[0]][index[1]].split("")).filter(x -> searchTerm.equals(x)).count();
                } else {
                    occurrenceCount = 1;
                }
                System.out.printf("[%d,%d] - %d Occurrence\n", index[0], index[1], occurrenceCount);
            }
        }
    }

    private static void edit(Scanner scanner) {
        System.out.print("Enter cell index (row,column): ");
        String[] indices = scanner.nextLine().split(",");
        int row = Integer.parseInt(indices[0]);
        int column = Integer.parseInt(indices[1]);
        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine();
        table[row][column] = String.format("%3s", newValue).replace(' ', '0');
        System.out.println("Cell updated.");
    }

    private static void print() {
        Arrays.stream(table)
                .forEach(row -> System.out.println(Arrays.toString(row)));
    }
}
