package at.htl.breaknotifierandroid.model;

import java.util.ArrayList;

public class Lesson
{
    private String subjects;
    private String startTime;
    private String endTime;
    private String suppliert;
    private String room;
    private String teacher;
    //One break unit = 5min => recess = 3 break units
    public Integer skipBreakUnits = 0;

    public String getSubjects(){
        return subjects;
    }
    public String getRoom() { return room; }

    public Lesson(String subjectsIn, String startTimeIn, String endTimeIn, String suppliertIn, String roomIn, String teacherIn)
    {
        subjects = subjectsIn;
        startTime = startTimeIn;
        endTime=endTimeIn;
        suppliert = suppliertIn;
        room = roomIn;
        teacher = teacherIn;
    }

    public Lesson(String subjectsIn, String startTimeIn, String endTimeIn, String roomIn, String teacherIn)
    {
        subjects = subjectsIn;
        startTime = startTimeIn;
        endTime=endTimeIn;
        suppliert = "";
        room = roomIn;
        teacher = teacherIn;
    }

    public String getEndTime(){
        return endTime;
    }

    public String toString() {

        return subjects + "\t\t" + startTime + "-" + endTime + "\t\t" + teacher + "\t\t\t" + room + "\t\t\t" + suppliert;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getStartTime()
    {
        return this.startTime;
    }

    public void copy(Lesson other) {
        this.subjects = other.subjects;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.suppliert = other.suppliert;
        this.room = other.room;
        this.teacher = other.teacher;
    }
}
