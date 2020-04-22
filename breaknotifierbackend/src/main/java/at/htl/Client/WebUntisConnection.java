package at.htl.Client;


import at.htl.Dashboard;
import at.htl.Data.PasswordEncrypt;
import at.htl.Data.Subjects;
import com.google.gson.Gson;

import javax.enterprise.inject.New;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import javax.ws.rs.client.Client;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WebUntisConnection implements Runnable {
    private final Long DELAY_FOR_RELOAD_OF_SUBJECTS = 900000l;
    private String id;
    private String uname;
    private String pw;
    private NewCookie cookie;
    List<Subjects> subjectsList = new LinkedList<>();
    private Boolean snooze = false;

    public boolean isRunnin() {
        return runnin;
    }

    private boolean runnin = true;

    public WebUntisConnection(String _id, String _uname, String _pw) {
        id = _id;
        uname = _uname;
        pw = _pw;
    }

    public NewCookie login() {
        Client client = ClientBuilder.newClient();
        WebTarget target;
        String serverUrl = "https://mese.webuntis.com/WebUntis/j_spring_security_check";
        try {
            //HttpPost post = new HttpPost("https://"+serverUrl+"/WebUntis/j_spring_security_check");
            target = client.target(serverUrl);
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("school", "htbla linz leonding");
            formData.add("j_username", PasswordEncrypt.decrypt(uname, id));
            formData.add("j_password", PasswordEncrypt.decrypt(pw, id));
            formData.add("token", "");


            try {
                Response response = target.request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(formData));
                String responseString = response.readEntity(String.class);

                if (response.getStatus() == 200 && responseString.contains("SUCCES")) {
                    Map<String, NewCookie> map = response.getCookies();
                    System.out.println("Erfolgreich");
                    return map.get("JSESSIONID"); //Speichert den Cookie vom Erfolgreichen Login
                }
            } catch (Exception e) {
                Dashboard.exceptions.add(new Exception("Falsches Passwort"));
                System.out.println("Falsches Passwort");
            }

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    public void snoozing(){
        snooze = !snooze;
    }

    public void Stopping() {
        runnin = false;

    }

    public void sendUserAndSubjects(){
        try {
            SendPushNotification.pushFCMNotification(id, "#100", PasswordEncrypt.decrypt(uname, id));
            readDailyHours();
            SendPushNotification.pushFCMNotification(id, "#200", new Gson().toJson(subjectsList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {



        cookie = login();
        if (cookie != null) {
            sendUserAndSubjects();
            long stopwatch = System.currentTimeMillis() - DELAY_FOR_RELOAD_OF_SUBJECTS;
            while (runnin) {
//                if(snooze){
//                    LocalDate date = LocalDate.now(ZoneId.of("CET"));
//                    date.plusDays(1);
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//                    String formattedDate = date.format(formatter);
//                    String compareDate = "";
//                    do {
//                        date = LocalDate.now(ZoneId.of("CET"));
//                        compareDate = date.format(formatter);
//                        try {
//                            Thread.sleep(300);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }while (formattedDate.equals(compareDate) && snooze);
//                    snooze = false;
//                }
                try {

                    if ((System.currentTimeMillis() - stopwatch) >= DELAY_FOR_RELOAD_OF_SUBJECTS) {
                        readDailyHours();
                        stopwatch = System.currentTimeMillis();
//                        System.out.println("Length of list: " + subjectsList.size());
                    }
                    if (subjectsList.size() > 0) {
                        noticeNotification();
//                        System.out.println("Length of list: " + subjectsList.size());
                    }
                    Thread.sleep(10000);
                } catch (Exception e) {
                    Dashboard.exceptions.add(e);
                    for (int i = 0; i < 5; i++) {
                        cookie = login();
                        if (cookie == null) {
                            Dashboard.exceptions.add(new Exception(i + " Try"));
                        } else {
                            i = 6;
                        }
                    }
                    if (cookie == null) {
                        break;
                    }
                }
            }
        }
        System.out.println("Thread Stopped");

    }


    private void noticeNotification() throws Exception {
        LocalDateTime time = LocalDateTime.now(ZoneId.of("CET"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = time.format(formatter);
        if (Integer.parseInt(formattedTime.replace(":", "")) ==
                Integer.parseInt(subjectsList.get(0).getEndTime().replace(":", ""))) {
//            System.out.println("Hour is over");
            SendPushNotification.pushFCMNotification(id, "Unterrichtsende", subjectsList.get(0).getSubject());
            Thread.sleep(3000);
            readDailyHours();
            SendPushNotification.pushFCMNotification(id, "#200", new Gson().toJson(subjectsList));
            Thread.sleep(65000);
            readDailyHours();
        } else if (Integer.parseInt(formattedTime.replace(":", "")) ==
                Integer.parseInt(subjectsList.get(0).getStartTime().replace(":", ""))) {
//            System.out.println("Hour starts");

            SendPushNotification.pushFCMNotification(id, "Unterrichtsbeginn", subjectsList.get(0).toString());


            Thread.sleep(65000);
            readDailyHours();
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
        LocalDate date = LocalDate.now(ZoneId.of("CET"));
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
        this.subjectsList = new LinkedList<>();
        for (int x = 0; x < jsonArray.size(); x++) {
            JsonObject jsonObject = jsonArray.getJsonObject(x);
//            System.out.println(jsonObject);
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
            String className = jsonObject.getString("klasse");

            //String backColor = jsonObject.getString("backColor");
            LocalDateTime time = LocalDateTime.now(ZoneId.of("CET"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = time.format(formatter);
            this.subjectsList.add(new Subjects(subject, room, startTime, endTime, teacher, className));
//            if (Integer.parseInt(formattedTime.replace(":", "")) <
//                    Integer.parseInt(endTime.replace(":", ""))) {
//                this.subjectsList.add(new Subjects(subject, room, startTime, endTime, teacher));
//            }
        }
    }
}
