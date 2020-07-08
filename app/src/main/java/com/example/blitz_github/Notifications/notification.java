package com.example.blitz_github.Notifications;


public class notification {

    private String title;
    private String message;
    private String time;


    public notification() {
    }

    public notification(String title, String message,String time) {

        this.title = title;
        this.message = message;
        this.time=time;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String  message) {
        this. message =  message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
