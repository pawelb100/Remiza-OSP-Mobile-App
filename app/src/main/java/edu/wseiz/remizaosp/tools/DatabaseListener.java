package edu.wseiz.remizaosp.tools;

public interface DatabaseListener {
    void onSuccess();
    void onFailed(Exception e);
}
