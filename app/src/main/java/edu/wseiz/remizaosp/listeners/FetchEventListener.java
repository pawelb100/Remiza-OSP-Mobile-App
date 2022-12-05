package edu.wseiz.remizaosp.listeners;

import edu.wseiz.remizaosp.models.Event;

public interface FetchEventListener {
    void onSuccess(Event event);
    void onNoData();
    void onFailed();
}
