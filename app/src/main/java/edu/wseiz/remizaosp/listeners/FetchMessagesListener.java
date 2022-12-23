package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.Message;

public interface FetchMessagesListener {
    void onSuccess(List<Message> messageList);
    void onFailed();
}
