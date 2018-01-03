package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model User
 */

public class User implements Serializable {

    private String username;

    private String password;

    private String name;

    private String phone;

    private String type;

    private String email;

    @SerializedName("api_key")
    private String apiKey;

    private List<String> courses;

    private List<String> assignments;

    public User(String usr, String psw, String nm, String ph, String typ, String eml) {
        username = usr;
        password = psw;
        name = nm;
        phone = ph;
        type = typ;
        email = eml;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getApiKey() { return apiKey; }
    public List<String> getCourses() { return courses; }
    public List<String> getAssignments() { return assignments; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setType(String type) { this.type = type; }
    public void setEmail(String email) { this.email = email; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setCourses(List<String> courses) { this.courses = courses; }
    public void setAssignments(List<String> assignments) { this.assignments = assignments; }
}
