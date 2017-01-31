package kh.edu.rupp.ckcc.eclass.vo;

/**
 * eClass
 * Created by leapkh on 3/1/17.
 */

public class Comment {

    private String course;
    private String text;
    private String image;

    public Comment() {

    }

    public Comment(String course, String text, String image) {
        this.text = text;
        this.image = image;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
