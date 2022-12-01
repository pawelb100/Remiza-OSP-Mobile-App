package edu.wseiz.remizaosp.listeners;


public interface FetchUserStatusIdListener {
    void onSuccess(String statusId);
    void onNoData();
    void onFailed();
}
