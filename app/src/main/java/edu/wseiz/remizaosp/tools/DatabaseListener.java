package edu.wseiz.remizaosp.tools;

import com.google.firebase.database.DatabaseError;

public interface DatabaseListener {
    void onSuccess();
    void onFailed(DatabaseError databaseError);
}
