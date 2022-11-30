package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.Availability;
import edu.wseiz.remizaosp.models.User;

public interface FetchAvailabilityListener {
    void onSuccess(List<Availability> availabilities);
    void onFailed();
}
