package edu.wseiz.remizaosp.listeners;

import java.util.List;

import edu.wseiz.remizaosp.models.Participation;

public interface FetchParticipationListListener {
    void onSuccess(List<Participation> participationList);
    void onNoData();
    void onFailed();
}
