package at.htl.breaknotifierandroid.model;

public class Lesson
{
    private String subjects;
    private String startTime;
    private String endTime;
    private String suppliert;
    private String room;
    private String teacher;

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

    public String toString() {

        return subjects + "\t\t" + startTime + "-" + endTime + "\t\t" + teacher + "\t\t\t" + room + "\t\t\t" + suppliert;
    }
}
