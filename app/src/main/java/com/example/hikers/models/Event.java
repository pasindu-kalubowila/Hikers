package com.example.hikers.models;

public class Event {

    private String eventid;
    private String eventimage;
    private String eventdescription;
    private String publisher;

    public Event() {

    }

    public Event(String eventid, String eventimage, String eventdescription, String publisher) {
        this.eventid = eventid;
        this.eventimage = eventimage;
        this.eventdescription = eventdescription;
        this.publisher = publisher;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getEventimage() {
        return eventimage;
    }

    public void setEventimage(String eventimage) {
        this.eventimage = eventimage;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventid='" + eventid + '\'' +
                ", eventimage='" + eventimage + '\'' +
                ", eventdescription='" + eventdescription + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
