package org.mongo.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "attendance")
public class Attendance {

    @Id
    private String id;
    private String username;
    private String entryTime;
    private String exitTime;
    private String date;
    private String totaltime;
    private String month;
    private String overtime;
    private String lateswipetime;
    private String lesstime;

    public String getLesstime() {
        return lesstime;
    }

    public void setLesstime(String lesstime) {
        this.lesstime = lesstime;
    }

    public String getLateswipetime() {
        return lateswipetime;
    }

    public void setLateswipetime(String lateswipetime) {
        this.lateswipetime = lateswipetime;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
