package edu.wseiz.remizaosp.tools;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.wseiz.remizaosp.models.User;

public class Database {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth fAuth;
    private FirebaseUser user;

    private List<String> statuses;
    private List<User> users;

    public Database()
    {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        statuses = new ArrayList<>();
        users = new ArrayList<>();
    }

    public List<String> getStatuses()
    {
        return statuses;
    }

    public List<User> getUsers()
    {
        return users;
    }


    public void updateStatus(int StatusId, final DatabaseListener listener) {
        reference.child("userstatus").child(user.getUid()).setValue(StatusId).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed(task.getException());
        });
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
                listener.onFailed(error.toException());
            }
        });
    }

    public void fetchUsers(final DatabaseListener listener) {

        reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    users.clear();

                    for(DataSnapshot dsUser : snapshot.getChildren())
                    {
                        User user = new User();
                        user.setUid(dsUser.getKey());
                        user.setName(dsUser.child("name").getValue(String.class));
                        user.setEmail(dsUser.child("email").getValue(String.class));
                        user.setRole(dsUser.child("role").getValue(String.class));

                        users.add(user);

                    }
                }

                listener.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed(error.toException());
            }
        });
    }

}
