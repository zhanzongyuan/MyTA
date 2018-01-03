package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model User
 */

public class User implements Serializable {

    private int id;

    private String username;
    
    private String password;

    private String name;

    @SerializedName("campus_id")
    private String campusID;

    private String phone;

    private String type;

    private String email;

    @SerializedName("api_key")
    private String apiKey;

    private List<String> courses;

    private List<String> assignments;

    public User() {}

    public int getID() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getCampusID() { return campusID; }
    public String getPhone() { return phone; }
    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getApiKey() { return apiKey; }
    public List<String> getCourses() { return courses; }
    public List<String> getAssignments() { return assignments; }

    public void setID(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setCampusID(String campusID) { this.campusID = campusID; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setType(String type) { this.type = type; }
    public void setEmail(String email) { this.email = email; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setCourses(List<String> courses) { this.courses = courses; }
    public void setAssignments(List<String> assignments) { this.assignments = assignments; }
}
