package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.Event;

public interface FetchEventsListener {
    void onSuccess(List<Event> events);
    void onFailed();
}
