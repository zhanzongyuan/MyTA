package four.awesome.myta.models;

/**
 * Model User
 */

public class User {
    public static int TYPE_STUDENT = 0, TYPE_TEACHER = 1;

    private String username;
    private String password;
    private int type;
    private String email;
    private String token;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getType() { return type; }
    public String getEmail() { return email; }
    public String getToken() { return token; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setType(int type) { this.type = type; }
    public void setEmail(String email) { this.email = email; }
    public void setToken(String token) { this.token = token; }
}
