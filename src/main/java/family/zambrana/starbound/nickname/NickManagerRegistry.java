package family.zambrana.starbound.nickname;

public class NickManagerRegistry {

    private static NickManager instance;

    public static void register(NickManager manager) {
        instance = manager;
    }

    public static NickManager get() {
        if (instance == null) {
            throw new IllegalStateException("NickManager has not been registered!");
        }
        return instance;
    }
}
