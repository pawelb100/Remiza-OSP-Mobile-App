package edu.wseiz.remizaosp.listeners;

import edu.wseiz.remizaosp.models.Participation;

public interface FetchParticipationListener {
    void onSuccess(Participation participation);
    void onNoData();
    void onFailed();
}
