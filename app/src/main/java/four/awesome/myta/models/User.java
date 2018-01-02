package four.awesome.myta.models;

/**
 * Model User
 */

public class User {
    private String username;
    private String password;
    private String type;
    private String email;
    private String apiKey;

    public User(String usrn, String psw, String typ, String eml) {
        username = usrn;
        password = psw;
        type = typ;
        email = eml;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getApiKey() { return apiKey; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setType(String type) { this.type = type; }
    public void setEmail(String email) { this.email = email; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}
