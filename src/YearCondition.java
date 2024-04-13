public class YearCondition implements Condition {
    private String title = "Year Check";
    private String description = "Check if the data is from the year ";
    private int count = 0;
    private int year;
    private int end;
    private boolean customDescription = false;
    public YearCondition(int year) {
        this.year = year;
        this.end = year;
    }
    public YearCondition(String year) {
        this.year = Integer.parseInt(year);
        this.end = this.year;
    }
    public YearCondition(int year, int end) {
        this.year = year;
        this.end = end;
    }
    public YearCondition(String year, String end) {
        this.year = Integer.parseInt(year);
        this.end = Integer.parseInt(end);
    }
    public YearCondition(int year, String description) {
        this.year = year;
        this.end = year;
        this.description = description;
        this.customDescription = true;
    }
    public YearCondition(String year, String end, String description) {
        this.year = Integer.parseInt(year);
        this.end = Integer.parseInt(end);
        this.description = description;
        this.customDescription = true;
    }
    public boolean run(String[] row) {
        // Check if row[2] (String) is between the year and end
        return Integer.parseInt(row[0]) >= this.year && Integer.parseInt(row[0]) <= this.end;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description + this.year;
    }
    public void increaseCount() {
        this.count++;
    }
    public int getCount() {
        return this.count;
    }
    public String toString() {
        if (customDescription) {
            return this.title + ": " + this.description + "\nCount: " + getCount() + " rows";
        }
        return this.title + ": " + this.description + this.year + "\nCount: " + getCount() + " rows";
    }
}
