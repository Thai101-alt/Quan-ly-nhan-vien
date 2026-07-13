package User;

public class UserSession {
    private static String currentUser; 

    public static void setCurrentUser(String manv) {
        currentUser = manv;
    }

    public static String getCurrentUser() {
        return currentUser;
    }
}
