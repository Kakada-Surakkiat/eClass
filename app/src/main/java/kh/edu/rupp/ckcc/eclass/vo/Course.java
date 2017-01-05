package kh.edu.rupp.ckcc.eclass.vo;

/**
 * eClass
 * Created by leapkh on 3/1/17.
 */

public class Course {

    private String name;
    private String description;
    private String category;

    public Course() {

    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
