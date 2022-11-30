package edu.wseiz.remizaosp.listeners;

import edu.wseiz.remizaosp.models.Availability;

public interface FetchCurrentUserAvailabilityListener {
    void onSuccess(Availability availability);
    void onFailed();
}
