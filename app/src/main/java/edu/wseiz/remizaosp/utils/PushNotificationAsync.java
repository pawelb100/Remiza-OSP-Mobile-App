package edu.wseiz.remizaosp.utils;

import android.os.AsyncTask;

import edu.wseiz.remizaosp.listeners.UpdateListener;

public class PushNotificationAsync extends AsyncTask<String, String, Boolean> {

    private final String title;
    private final String message;
    private final UpdateListener listener;


    public PushNotificationAsync(String title, String message, UpdateListener listener) {
        this.title = title;
        this.message = message;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {

            NotificationSender sender = new NotificationSender();
            return sender.push(title, message);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result)
            listener.onSuccess();
        else
            listener.onFailed();

    }
}
