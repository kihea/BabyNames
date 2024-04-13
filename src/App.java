import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.io.FileWriter;

public class App {
    static long start;
    static long end;
    static Condition[] conditions;
    public static void main(String[] args) throws Exception {
        startBenchmark();

        fillConditions();
        Analytics analytics = new Analytics(conditions);
        Analytics analytics2 = new Analytics();
        Scanner reader = initScanner("./names.csv");
        Scanner reader2 = initScanner("./first_nameRaceProbs.csv");
        FileWriter writer = initWriter("../bin/names.txt");
        FileWriter writer2 = initWriter("../bin/names2.csv");
        if (reader == null || reader2 == null || writer == null) {
            return;
        }
        writer.write("Year Count Sex Name Race\n");
        writer2.write("Name,Sex,Count,Year,Race\n");
        while (reader2.hasNextLine()) {
            String data = reader2.nextLine();
            String[] values = data.split(",");
            analytics2.parseRow(values); // Only using second analytics for storing name->race% data
        }
        HashMap<String, Integer> unisexNames = new HashMap<String, Integer>();
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            String[] values = data.split(",");
             // Sends each row through each of the conditions
            int corspRow = analytics2.findRow(0, values[0].toUpperCase());
            if (corspRow != -1) {
                String[] row = analytics2.getRow(corspRow);
                // Find value with largest probability
                double max = 0;
                int index = 0;
                String name = "";
                for (int i = 1; i < row.length; i++) {
                    if (Double.parseDouble(row[i]) > max) {
                        max = Double.parseDouble(row[i]);
                        index = i;
                    }
                }
                switch (index) {
                    case 1:
                        name = "White";
                        break;
                    case 2:
                        name = "Black";
                        break;
                    case 3:
                        name = "Hispanic";
                        break;
                    case 4:
                        name = "Asian";
                        break;
                    case 5:
                        name = "Other";
                        break;
                }
                String[] appendedOutput = new String[]{values[0], values[1], values[2], values[3], name};
                String[] reorderedOutput = new String[]{values[3], values[2], values[1], values[0], name}; 
                //                                      Year       Count      Gender     Name       Race
                analytics.parseRow(reorderedOutput);
                int count = analytics.getCount(3, values[0]); // Find how many names there are
                if (count > 1) {
                    unisexNames.put(values[0], count);
                }
                writer.write(String.join(" ", reorderedOutput) + "\n");
                writer2.write(String.join(",", appendedOutput) + "\n");
            } else {
                //System.out.println("No data found for " + values[0]);
                String[] appendedOutput = new String[]{values[0], values[1], values[2], values[3], "Black"};
                String[] reorderedOutput = new String[]{values[3], values[2], values[1], values[0], "Black"};  // According to the data
                //                                      Year       Count      Gender     Name       Race
                analytics.parseRow(reorderedOutput);
                int count = analytics.getCount(3, values[0]); // Find how many names there are
                if (count > 1) {
                    unisexNames.put(values[0], count);
                }
                writer.write(String.join(" ", reorderedOutput) + "\n");
                writer2.write(String.join(",", appendedOutput) + "\n");
            }
           
            // If the condition passes, the count increases (usually by 1, but can be changed to use an index in the row array)
        }
        boolean verbose = true;
        {
            // Loop over all unisex names (unfiltered) and only keep the ones that occur with both M and F gender
            // Then, print the name
            System.out.println("Unisex names: ");
            String nameRow = "";
            int threshold = 250; 
            for (String key : unisexNames.keySet()) {
                boolean hasMale = false;
                boolean hasFemale = false;
                for (int i = 0; i < unisexNames.get(key); i++) { // Loop over all rows with the name
                    String[] row = analytics.getRow(analytics.findRow(3, key, i)); // We know the row exists, so no need to check for -1
                    if (row[2].equals("M")) hasMale = true;
                    if (row[2].equals("F")) hasFemale = true;
                    if (hasMale && hasFemale && verbose) {
                        if (nameRow.equals("")) {
                            nameRow += key;
                        } else {
                            nameRow += ", " + key;
                        }
                        if (nameRow.length() > threshold) {
                            System.out.println(nameRow);
                            nameRow = "";
                        }
                        break;
                    }
                }
            }
        }  
        {
            System.out.println("\nMost popular names by year: ");
            HashMap<Integer, String[]> mostPopular = new HashMap<Integer, String[]>(); 
            String[] allYears = analytics.getKeys(0);
            for (String year : allYears) {
                int max = 0;
                String[] highestRow = new String[0];
                for (Integer corspRow : analytics.getRows(0, year)) {
                    String[] row = analytics.getRow(corspRow);
                    if (Integer.parseInt(row[1]) > max) {
                        max = Integer.parseInt(row[1]);
                        highestRow = row;
                    }
                }
                mostPopular.put(Integer.parseInt(year), highestRow);
            }
            String lastYearName = "";
            int firstYear = 0;
            String[] latestRow = new String[0];
            int latestYear = 2021;
            int totalCount = 0;
            int avgCount = 1;
            for (Integer year : mostPopular.keySet()) {
                String[] row = mostPopular.get(year);
                // Group by consecutive year for concise output
                if (firstYear == 0) {
                    firstYear = year;
                    lastYearName = row[3];
                    totalCount = Integer.parseInt(row[1]);
                    continue;
                } else {
                    if (lastYearName.equals(row[3])) {
                        totalCount += Integer.parseInt(row[1]);
                        avgCount++;
                        latestRow = row;
                        latestYear = year;
                        continue;
                    } else { 
                        totalCount += Integer.parseInt(row[1]);
                        avgCount++;
                        if (firstYear == year - 1) {
                            System.out.println(firstYear + ": " + lastYearName 
                            + " (" + latestRow[2] + ", " + latestRow[4] + ")" + " -" 
                            + " Total: " + String.valueOf(totalCount));
                        } else {
                            System.out.println(firstYear + "-" + (year-1)  + ": " + lastYearName 
                            + " (" + latestRow[2] + ", " + latestRow[4] + ")" 
                            + " - Avg: "  + String.valueOf(totalCount/avgCount) 
                            + " Total: " + String.valueOf(totalCount));
                        }
                        firstYear = year;
                        lastYearName = row[3];
                        latestRow = row;
                        continue;
                    }
                }
            }
            if (firstYear == latestYear) {
                System.out.println(latestYear + ": " + latestRow[3] 
                + " (" + latestRow[2] + ", " + latestRow[4] + ")" 
                + " - Total: " + String.valueOf(totalCount));
            } else {
                System.out.println(firstYear + "-" + latestYear  
                + ": " + latestRow[3] + " (" + latestRow[2] + ", " + latestRow[4] + ")" 
                + " - Avg: "  + String.valueOf(totalCount/avgCount) 
                + " Total: " + String.valueOf(totalCount));
            }
        }
        {
            System.out.println("\nLeast popular names by race: ");
            String[] allRaces = new String[]{"White", "Black", "Hispanic", "Asian", "Other"};
            for (String race: allRaces) {
                int min = Integer.MAX_VALUE;
                String minName = "";
                String[] minRow = new String[0];
                for (Integer corspRow : analytics.getRows(4, race)) {
                    String[] row = analytics.getRow(corspRow);
                    if (Integer.parseInt(row[1]) < min) {
                        min = Integer.parseInt(row[1]);
                        minRow = row;
                        minName = row[3];
                    }
                }
                System.out.println(race + " (" + minRow[0] + "): " + minName 
                + " (" + minRow[2] + ", " + minRow[4] + ")" + " - "  + minRow[1]);
            }
        }
        System.out.println();
        analytics.printResults(); // Baby Names 1 End
        endBenchmark();
        reader.close();
        reader2.close();
        writer.close();
    }
    public static FileWriter initWriter(String path) {
        try {
            FileWriter writer = new FileWriter(path);
            return writer;
        } catch (IOException error) {
            System.out.println("An error occurred.");
            error.printStackTrace();
            return null;
        }
    }
    public static Scanner initScanner(String path) {
        try {
            Scanner reader = new Scanner(new File(path));
            @SuppressWarnings("unused")
            String title = reader.nextLine(); // Skip the first line
            return reader;
        } catch (IOException error) {
            System.out.println("An error occurred.");
            error.printStackTrace();
            return null;
        }
    }
    public static void fillConditions() {
        conditions = new Condition[] {
                new CombinedCondition("How many rows of data are there?"), // Total amount of rows
                new CheckLetter("X", "How many rows start with the letter X?"), // Number of rows with X
                new CombinedCondition("How many male names start with the letter Z?",new CheckGender("M"), new CheckLetter("Z")),
                new YearCondition(2000, "How many people are born in the year 2000?"),
                new AlteredCountCombinedCondition(1, "What is the total count (index = 2) of people that start with the letter X?", 
                new CheckLetter("X")), // Total count of rows with X
        };
    }
    public static void startBenchmark() {
        start = System.currentTimeMillis();
    }
    public static void endBenchmark() {
        if (start == 0) {
            System.out.println("Benchmark not started");
            return;
        }
        end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
    }
    public static void resetBenchmark() {
        start = 0;
        end = 0;
    }
}