package com.gngsn.springcore.dataBinding;

public class Event {
    private Integer id;

    private String title;

    public Event(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", title='" + title + '\'' +
            '}';
    }
}
