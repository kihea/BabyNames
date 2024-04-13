public interface Condition {
    boolean run(String[] row);
    String getTitle();
    String getDescription();
    void increaseCount();
    int getCount();
    String toString();
}
