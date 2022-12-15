package edu.wseiz.remizaosp.listeners;

import edu.wseiz.remizaosp.models.Role;

public interface FetchUserRoleListener {
    void onSuccess(Role role);
    void onFailed();
}
