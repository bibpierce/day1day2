
import javax.naming.PartialResultException;
import java.util.*;
import java.util.stream.Collectors;

public class HomeworkDayTwo {

    public static Map<String, String>[][] table;

    public static void main(String[] args) {

        int[] ex = {3,3,3,3,2,1,3,5,6,7};
        System.out.println(Arrays.stream(ex).filter(x -> x==3).count());


        Scanner scanner = new Scanner(System.in);
        resetTable(scanner);
        while (true) {
            System.out.println("Enter an input (search|edit|print|reset|exit):");
            String input = scanner.nextLine();
            switch (input) {
                case "search" -> search(scanner);
                case "edit" -> edit(scanner);
                case "sort" -> sortTable(scanner);
                case "add" -> addColumnToRow(scanner);
                case "print" -> print();
                case "reset" -> resetTable(scanner);
                case "exit" -> System.exit(0);
                default -> System.out.println("Invalid input.");
            }
        }
    }

    private static void edit(Scanner scanner) {

        System.out.print("Enter cell index (row,column): ");
        String[] indices = scanner.nextLine().split(",");
        int rowIndex = Integer.parseInt(indices[0]);
        int colIndex = Integer.parseInt(indices[1]);

        System.out.print("Enter (K)ey, (V)alue to edit: ");
        String editOption = scanner.nextLine().toLowerCase();

        System.out.print("Enter the new value: ");
        String newValue = scanner.nextLine();

        if (rowIndex < 0 || rowIndex >= table.length || colIndex < 0 || colIndex >= table[rowIndex].length) {
            System.out.println("Invalid row or column index.");
            return;
        }

        Map<String, String> cell = table[rowIndex][colIndex];
        if (editOption.equals("k")) {
            String oldKey = cell.keySet().iterator().next();
            String oldValue = cell.get(oldKey);
            cell.remove(oldKey);
            cell.put(newValue, oldValue);
        }
        if (editOption.equals("v")) {
            String oldKey = cell.keySet().iterator().next();
            cell.put(oldKey, newValue);
        }
        System.out.println("Cell edited successfully.");
    }


    public static void addColumnToRow(Scanner scanner) {
        System.out.print("Enter row index to add column to: ");
        int rowIndex = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter value to add: ");
        String value = scanner.nextLine();
        System.out.print("Enter key to add: ");
        String key = scanner.nextLine();

        if (rowIndex < 0 || rowIndex >= table.length) {
            System.out.println("Invalid row index.");
            return;
        }

        Map<String, String>[] newRow = Arrays.copyOf(table[rowIndex], table[rowIndex].length + 1);
        newRow[newRow.length - 1] = new HashMap<>();
        newRow[newRow.length - 1].put(key, value);

        table[rowIndex] = newRow;

    }

    public static void resetTable(Scanner scanner) {
        System.out.print("Enter table dimensions (rows x columns ex: 3x3): ");
        String[] size = scanner.nextLine().split("x");

        int rows = Integer.parseInt(size[0]);
        int columns = Integer.parseInt(size[1]);

        table = new Map[rows][columns];
        Arrays.setAll(table, i -> Arrays.stream(new Map[columns]).map(j -> new HashMap<String, String>()).toArray(Map[]::new));
        Arrays.stream(table).flatMap(Arrays::stream).forEach(map -> map.put(getRandomString(3), getRandomString(3)));
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
        return String.format("%3s", sb);
    }

    private static void print() {
        Arrays.stream(table)
                .forEach(row -> System.out.println(Arrays.toString(row)));
    }

    private static void sortTable(Scanner scanner) {
        System.out.print("Sort by (A)scending or (D)escending: ");
        String sortOrder = scanner.nextLine().toLowerCase();


        Comparator<String> comparator = sortOrder.equals("a") ? Comparator.naturalOrder() : Comparator.reverseOrder();

        for (Map<String, String>[] row : table) {
            Arrays.sort(row, (a, b) -> comparator.compare(
                    a.entrySet().stream().map(e -> e.getKey() + e.getValue()).collect(Collectors.joining()),
                    b.entrySet().stream().map(e -> e.getKey() + e.getValue()).collect(Collectors.joining())
            ));
        }
    }

    private static void search(Scanner scanner) {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        int count = 0;
        List<int[]> keyIndices = new ArrayList<>();
        List<int[]> valueIndices = new ArrayList<>();

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                String concatKV = "";
                for (Map.Entry<String, String> entry : table[i][j].entrySet()) {
                    concatKV += entry.getKey() + entry.getValue();
                }

                if (concatKV.contains(searchTerm)) {
                    count++;
                    if (concatKV.indexOf(searchTerm) < table[i][j].entrySet().iterator().next().getKey().length()) {
                        keyIndices.add(new int[]{i, j});
                    } else {
                        valueIndices.add(new int[]{i, j});
                    }
                }
            }
        }

        if (count == 0) {
            System.out.println("No occurrences found.");
        } else {
            System.out.printf("%d occurrence(s) found:\n", count);
            for (int[] index : keyIndices) {
                long keyCount;
                if(searchTerm.length() == 1) {
                    keyCount = Arrays.stream(table[index[0]][index[1]].keySet().toString().split("")).filter(x -> searchTerm.equals(x)).count();
                } else{
                    keyCount = 1;
                }
                System.out.printf("[%d,%d] - %d Occurence(s) found on key field\n", index[0], index[1], keyCount);
            }
            for (int[] index : valueIndices) {
                long valCount;
                if(searchTerm.length() == 1) {
                    valCount = Arrays.stream(table[index[0]][index[1]].values().toString().split("")).filter(x -> searchTerm.equals(x)).count();
                } else{
                    valCount = 1;
                }
                System.out.printf("[%d,%d] - %d Occurence(s) found on value field\n", index[0], index[1], valCount);
            }
        }
    }


}
