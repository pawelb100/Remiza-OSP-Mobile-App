package edu.wseiz.remizaosp.listeners;

public interface AddEventListener {
    void onSuccess(String generatedId);
    void onFailed();
}
