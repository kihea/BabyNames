public class CheckGender implements Condition {
    private String title = "Gender Check";
    private String description = "Check if the row of data refers to ";
    private int count = 0;
    private String gender;

    public CheckGender(String gender) {
        this.gender = gender;
    }
    public CheckGender(String gender, String description) {
        this.gender = gender;
        this.description = description;
    }
    public boolean run(String[] row) {
        return row[2].equals(this.gender);
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description + this.gender;
    }
    public void increaseCount() {
        this.count++;
    }
    public int getCount() {
        return this.count;
    }
    public String toString() {
        return this.title + ": " + this.description + this.gender + "\nCount: " + getCount() + " rows";
    }
}
