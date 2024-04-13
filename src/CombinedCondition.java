public class CombinedCondition  implements Condition {
    private String title = "Combined Condition";
    private String description = "Check if the data meets all conditions:";
    private Condition[] conditions;
    private int count = 0;
    public CombinedCondition(Condition... conditions) {
        this.conditions = conditions;
    }
    public CombinedCondition(String description, Condition... conditions) {
        this.description = description;
        this.conditions = conditions;
    }
    public boolean run(String[] row) {
        for (Condition condition : conditions) {
            if (!condition.run(row)) {
                return false;
            }
        }
        return true;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        String description = "\t";
        for (Condition condition : conditions) {
            description += condition.getTitle() + ": " + condition.getDescription() + "\n\t";
        }
        if (description.length() == 1) {
            return this.description + "\n\tNo conditions";
        }
        return this.description + "\n" + description.substring(0, description.length() - 2); // remove last \n\t
    }
    public void increaseCount() {
        this.count++;
    }
    public int getCount() {
        return this.count;
    }
    public String toString() {
        return this.title + ": " + getDescription() + "\nCount: " + getCount() + " rows";
    }
}
