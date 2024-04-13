public class OverallCondition implements Condition {
    private String title = "Overall Check";
    private String description = "Check if the data is ";
    private int count = 0;
    private String condition;
    private Condition[] conditions;

    public OverallCondition(String condition, Condition[] conditions) {
        this.condition = condition;
        this.conditions = conditions;
    }
    
    public boolean run(String[] row) {
        boolean result = false;
        if (this.condition.equals("AND")) {
            result = true;
            for (Condition condition : this.conditions) {
                result = result && condition.run(row);
            }
        } else if (this.condition.equals("OR")) {
            result = false;
            for (Condition condition : this.conditions) {
                result = result || condition.run(row);
            }
        }
        return result;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description + this.condition;
    }
    public void increaseCount() {
        this.count++;
    }
    public int getCount() {
        return this.count;
    }
    public String toString() {
        return this.title + ": " + this.description + this.condition + "\nCount: " + getCount() + " rows";
    }
}
