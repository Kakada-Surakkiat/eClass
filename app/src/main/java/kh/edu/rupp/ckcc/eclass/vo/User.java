package kh.edu.rupp.ckcc.eclass.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * eClass
 * Created by leapkh on 15/12/16.
 */

public class User {

    private int id;
    private String name;
    private String gender;
    private String email;
    private String dob;

    public static User fromJson(JSONObject object){
        User user = new User();
        try {
            user.name = object.getString("name");
            user.gender = object.getString("gender");
            user.email = object.getString("email");
            user.dob = object.getString("birthday");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
