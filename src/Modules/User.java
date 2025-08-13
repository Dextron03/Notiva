package Modules;

public class User {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private Sesion lastSesion;

    // Usuario activo en memoria
    private static User currentUser;

    public User(String userName, String email, String password, Sesion lastSesion) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.lastSesion = lastSesion;
    }

    public User(int userId, String userName, String email, String password, Sesion lastSesion) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.lastSesion = lastSesion;
    }

    // Saber si está activo
    public boolean isActive() {
        return lastSesion != null && lastSesion.isLoggedIn();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserIdString() {
    	return ""+userId;
    }
    
    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Sesion getLastSesion() {
        return lastSesion;
    }
    
    // Métodos para el usuario activo global
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    
    public int getCurrentUserId() {
    	return currentUser.getUserId();
    }
    
    
    
   
}
