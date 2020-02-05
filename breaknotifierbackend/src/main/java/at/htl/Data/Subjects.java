package at.htl.Data;

public class Subjects {
    public String getSubject() {
        return subject;
    }

    public String getRoomNr() {
        return roomNr;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTeacher() {
        return teacher;
    }

    private String subject;
    private String roomNr;
    private String startTime;
    private String endTime;
    private String teacher;

    public Subjects(String subject, String roomNr, String startTime, String endTime, String teacher) {
        this.subject = subject;
        this.roomNr = roomNr;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacher = teacher;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return subject + " - " + roomNr;
    }
}
