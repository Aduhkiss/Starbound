package family.zambrana.starbound.database;

public class DatabaseHolder {
    private static Database instance;

    public static void set(Database db) {
        instance = db;
    }

    public static Database get() {
        return instance;
    }
}
