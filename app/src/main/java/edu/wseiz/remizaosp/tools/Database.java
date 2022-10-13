package edu.wseiz.remizaosp.tools;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private List<String> statuses;

    public Database()
    {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        statuses = new ArrayList<String>();
    }

    public List<String> getStatuses()
    {
        return statuses;
    }

    public void fetchStatuses(final DatabaseListener listener) {

        reference.child("statuses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    statuses.clear();

                    for(DataSnapshot snapshotChild : snapshot.getChildren())
                    {
                        statuses.add(snapshotChild.getValue(String.class));
                    }
                }

                listener.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed(error);
            }
        });
    }

}
