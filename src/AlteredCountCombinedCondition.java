public class AlteredCountCombinedCondition implements Condition {
    private String title = "Combined Condition";
    private String description = "Combined Condition with altered count (uses row[3])";
    private int count = 0;
    private Condition[] conditions;
    private int index = 0;
    private int countStep = 0;
    public AlteredCountCombinedCondition(int count, Condition... conditions) {
        this.conditions = conditions;
        this.index = count;
    }
    public AlteredCountCombinedCondition(int count, String description) {
        this.description = description;
        this.index = count;
    }
    public AlteredCountCombinedCondition(int count, String description, Condition... conditions) {
        this.description = description;
        this.index = count;
        this.conditions = conditions;
    }
    public boolean run(String[] row) {
        countStep = Integer.parseInt(row[index]);
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
        return this.description + "\n" + description.substring(0, description.length() - 2);
    }
    public void increaseCount() {
        this.count += countStep;
    }
    public int getCount() {
        return this.count;
    }
    public String toString() {
        return this.title + ": " + getDescription() + "\nCount: " + getCount() + " count";
    }
}
