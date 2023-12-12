package com.example.demo;

public class CourseData {

    private String courseName;
    private String courseTitle;
    private String courseDescription;
    private String courseWorkTime;
    private String courseWorkDate;
    public CourseData(String courseName, String courseTitle, String courseDescription, String courseWorkTime, String courseWorkDate) {
        this.courseName = courseName;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseWorkTime = courseWorkTime;
        this.courseWorkDate = courseWorkDate;
    }
    public CourseData() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseWorkTime() {
        return courseWorkTime;
    }

    public void setCourseWorkTime(String courseWorkTime) {
        this.courseWorkTime = courseWorkTime;
    }

    public String getCourseWorkDate() {
        return courseWorkDate;
    }

    public void setCourseWorkDate(String courseWorkDate) {
        this.courseWorkDate = courseWorkDate;
    }
}
