package com.gngsn.springcore.dataBinding;

import java.beans.PropertyEditorSupport;

// old ...
public class EventEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        System.out.println("EventEditor - getAsText");
        Event event = (Event) getValue();
        return event.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("EventEditor - setAsText");
        setValue(new Event(Integer.parseInt(text)));
    }
}
