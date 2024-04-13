import java.util.ArrayList;
import java.util.HashMap;

public class Analytics {
    Condition[] conditions;
    ArrayList<String[]> data = new ArrayList<String[]>();
    ArrayList<HashMap<String, ArrayList<Integer>>> results = new ArrayList<HashMap<String, ArrayList<Integer>>>();
    // We store each row (name, year, ... ) in a hashmap that maps each index in the row to a key ({name: [...], year: [...], ...})
    // The values are an arraylist of integers that represent the index of the row in the data array
    // The data array is a list of all the rows sent to the analytics object using the parseRow method
    // This way we can quickly find all the rows that have a certain value in a certain column, basically a primitive query system
    // Overall, the Analytics class is the way we store, query, and analyze (using the Condition class) the data, trying mostly to reduce redundancy
    // The total compile time goes down significantly when doing this, as we don't have to reparse the data every time we want to do a query

    public Analytics(Condition[] rowConsumer) {
        conditions = rowConsumer;
    }
    public Analytics() {
        conditions = new Condition[0];
    }
    public void parseRow(String[] row) {
        if (results.size() == 0) { // if we haven't stored any data yet, we need to initialize the results array
            for (int i = 0; i < row.length; i++) {
                results.add(new HashMap<String, ArrayList<Integer>>());
            }
        }
        for (Condition condition : conditions) { // Analyze the row using the conditions
            if (condition.run(row)) {
                condition.increaseCount();
            }
        }
        data.add(row); // store the row data in an arraylist
        int index = data.size() - 1;
        for (int i = 0; i < row.length; i++) { // store the row data in a hashmap
            if (results.get(i).containsKey(row[i])) {
                ArrayList<Integer> result = results.get(i).get(row[i]);
                result.add(index);
            } else {
                ArrayList<Integer> result = new ArrayList<Integer>();
                result.add(index);
                results.get(i).put(row[i], result);
            }
        }
    }
    public int findRow(int index, String value) {
       return findRow(index, value, -1);
    }
    public int findRow(int index, String value, int overflow) { // find the index of the row that has a certain value in a certain column
        // overflow is used to get a certain row if there are multiple rows with the same value (-1 = last row)
        if (index < 0 || index >= results.size()) {
            return -1;
        }
        if (results.get(index).containsKey(value)) {
            ArrayList<Integer> result = results.get(index).get(value);
            if (overflow == -1) {
                overflow = result.size() - 1;
            }
            return result.get(overflow);
        }
        return -1;
    }
    public int getCount(int index, String value) { // retrieves the number of rows that the hashmap has stored for a certain value
        if (index < 0 || index >= data.size()) {
            return 0;
        }
        return results.get(index).get(value).size();
    }
    public ArrayList<Integer> getRows(int index, String value) { // retrieves the list of rows that the hashmap has stored for a certain value
        if (index < 0 || index >= results.size()) {
            return null;
        }
        if (results.get(index).containsKey(value)) {
            return results.get(index).get(value);
        }
        return null;
    }
    public String[] getKeys(int index) { // retrieves the keys from the hashmap (corresponds to the row representation in the data array)
        if (index < 0 || index >= results.size()) {
            return new String[0];
        }
        return results.get(index).keySet().toArray(new String[0]);
    }
    public String[] getRow(int index) { // retrieves the row data from the data array
        if (index < 0 || index >= data.size()) {
            return new String[0];
        }
        return data.get(index);
    }
    public void printResults() {
        for (Condition condition : conditions) {
            System.out.println(condition);
            System.out.println();
        }
    }
}
