
public class CheckLetter implements Condition {
    private String title = "Letter Check";
    private String description = "Check if the data contains the letter ";
    private String letter;
    private int index = 0;
    public int count = 0;
    private boolean customDescription = false;
    public CheckLetter(String letter) {
        this.letter = letter;
    }
    public CheckLetter(String letter, int index) {
        this.letter = letter;
        this.index = index;
    }
    public CheckLetter(String letter, int index, String description) {
        this.letter = letter;
        this.index = index;
        this.description = description;
        this.customDescription = true;
    }
    public CheckLetter(String letter, String description) {
        this.letter = letter;
        this.description = description;
        this.customDescription = true;
    }
    public boolean run(String[] row) {
        // CHeck if row[0] (String) contains the letter 
        if (this.index == -2) { // -2 means contains -1 can be used to check if it does not contain
            return row[3].contains(this.letter);
        }
        return row[3].indexOf(this.letter) == this.index;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description + this.letter + " at index " + this.index;
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
        return this.title + ": " + this.description + this.letter + " at index " + this.index + "\nCount: " + getCount() + " rows";
    }
}
