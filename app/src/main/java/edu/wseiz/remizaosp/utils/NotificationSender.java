package edu.wseiz.remizaosp.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NotificationSender {

    private static final String ONESIGNAL_APP_ID = "a5ab8de1-586c-4d80-a15f-60eb0111b4ad";
    private static final String ONESIGNAL_REST_API_KEY = "NWY2MjRiYjgtZWEzMC00ZmEzLWJkZjItNmIwMzEzODA4Zjdi";

    public NotificationSender() {}

    public boolean push(String title, String message) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic "+ ONESIGNAL_REST_API_KEY);
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"" + ONESIGNAL_APP_ID + "\","
                    +   "\"included_segments\": [\"Subscribed Users\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"" + message + "\"},"
                    +   "\"headings\": {\"en\": \"" + title + "\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);


            if (  httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
                System.out.println("jsonResponse:\n" + jsonResponse);
                return true;
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
                System.out.println("jsonResponse:\n" + jsonResponse);
                return false;
            }



        } catch(Throwable t) {
            t.printStackTrace();
        }
        return false;
    }
}
