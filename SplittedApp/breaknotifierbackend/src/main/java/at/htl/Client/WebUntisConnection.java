package at.htl.Client;


import at.htl.Data.Subjects;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class WebUntisConnection implements Runnable {
    private final Long DELAY_FOR_RELOAD_OF_SUBJECTS = 900000l;
    private NewCookie cookie;
    private String id;
    List<Subjects> subjectsList = new LinkedList<>();

    public WebUntisConnection(String _id, NewCookie _cookie) {
        id = _id;
        cookie = _cookie;
    }

    @Override
    public void run() {


        //To-Do
        long stopwatch = System.currentTimeMillis() - DELAY_FOR_RELOAD_OF_SUBJECTS;
        try {

            while (true) {
                //System.out.println((System.currentTimeMillis() - stopwatch));
                if (subjectsList.size() > 0) {
                    LocalDateTime time = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    String formattedTime = time.format(formatter);
                    if(Integer.parseInt(formattedTime.replace(":","")) >=
                            Integer.parseInt(subjectsList.get(0).getEndTime().replace(":",""))){
                        // System.out.println("Hour is over");
                        readDailyHours();
                        SendPushNotification.pushFCMNotification(id, "Stunde ist vorbei", subjectsList.get(0).getSubject());
                        stopwatch = System.currentTimeMillis();
                    } else if(Integer.parseInt(formattedTime.replace(":","")) ==
                            Integer.parseInt(subjectsList.get(0).getStartTime().replace(":",""))){
                        // System.out.println("Hour is over");
                        readDailyHours();
                        SendPushNotification.pushFCMNotification(id, "Stunden Beginn", subjectsList.get(0).toString());
                        stopwatch = System.currentTimeMillis();
                    }
                }
                if ((System.currentTimeMillis() - stopwatch) >= DELAY_FOR_RELOAD_OF_SUBJECTS) {
                    readDailyHours();
                    stopwatch = System.currentTimeMillis();
                    //return;
                }
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }


    private void readDailyHours() {
        Client client = ClientBuilder.newClient();
        WebTarget target;
        Response response;
        target = client.target("https://mese.webuntis.com/WebUntis/api/app/config");
        response = target.request(MediaType.APPLICATION_JSON).cookie(cookie).get();
        JsonObject object = response.readEntity(JsonObject.class);
        object = object.getJsonObject("data");
        object = object.getJsonObject("loginServiceConfig");
        object = object.getJsonObject("user");
        int personId = object.getInt("personId");
        int roleId = object.getInt("roleId");
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = date.format(formatter);
        String url = "https://mese.webuntis.com/WebUntis/api/daytimetable/dayLesson?date=" + formattedDate
                + "&id=" + personId + "&type=" + roleId;
        target = client.target(url);
        response = target.request(MediaType.APPLICATION_JSON).cookie(cookie).get();
        JsonArray jsonArray = response.readEntity(JsonObject.class)
                .getJsonObject("data")
                .getJsonArray("dayTimeTable");
        getDailyHours(jsonArray);
    }


    public void getDailyHours(JsonArray jsonArray) {
        for (int x = 0; x < jsonArray.size(); x++) {
            JsonObject jsonObject = jsonArray.getJsonObject(x);
            String subject = jsonObject.getString("subject");
            String startTime = String.valueOf(jsonObject.getInt("startTime"));
            String subTime = startTime.substring(startTime.length() - 2);
            String subTimePart1 = startTime.substring(0, startTime.length() - 2);
            startTime = subTimePart1 + ":" + subTime;

            String endTime = String.valueOf(jsonObject.getInt("endTime"));
            subTime = endTime.substring(endTime.length() - 2);
            subTimePart1 = endTime.substring(0, endTime.length() - 2);
            endTime = subTimePart1 + ":" + subTime;

            String teacher = jsonObject.getString("teacher");
            String room = jsonObject.getString("room");


            String backColor = jsonObject.getString("backColor");
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = time.format(formatter);
            //this.subjectsList.add(new Subjects(subject, room, startTime, endTime, teacher));
            if(Integer.parseInt(formattedTime.replace(":","")) <
                    Integer.parseInt(endTime.replace(":",""))){
                this.subjectsList.add(new Subjects(subject,room,startTime,endTime,teacher));
            }
        }
    }
}
