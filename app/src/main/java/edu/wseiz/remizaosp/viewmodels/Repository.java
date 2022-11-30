package edu.wseiz.remizaosp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.wseiz.remizaosp.listeners.FetchAvailabilityListener;
import edu.wseiz.remizaosp.listeners.FetchCurrentUserAvailabilityListener;
import edu.wseiz.remizaosp.listeners.FetchStatusListListener;
import edu.wseiz.remizaosp.listeners.FetchStatusListener;
import edu.wseiz.remizaosp.listeners.FetchUsersListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Availability;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.models.User;

public class Repository extends AndroidViewModel {

    private final FirebaseDatabase database;
    private final FirebaseAuth auth;

    public Repository(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    public void updateStatus(String StatusId, UpdateListener listener) {
        if (auth.getUid()!=null) {
            database.getReference().child("availability").child(auth.getUid()).setValue(StatusId).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                    listener.onSuccess();
                else
                    listener.onFailed();
            });
        }
    }

    public void fetchStatus(Availability availability, FetchStatusListener listener) {

        database.getReference().child("availability").child(availability.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String statusId = snapshot.getValue(String.class);

                    database.getReference().child("status").child(statusId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Status status = null;
                            if (snapshot.exists())
                                status = new Status(statusId, snapshot.getValue(String.class));

                            listener.onSuccess(status);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            listener.onFailed();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }

    public void fetchCurrentUserAvailability(FetchCurrentUserAvailabilityListener listener) {
        if (auth.getUid()!=null) {
            database.getReference().child("availability").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Availability availability;
                    if(snapshot.exists())
                        availability = new Availability(snapshot.getKey(), snapshot.getValue(String.class));
                    else
                        availability = null;
                    listener.onSuccess(availability);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailed();
                }
            });
        }
    }


    public void fetchStatusList(FetchStatusListListener listener) {

        database.getReference().child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Status> statuses = new ArrayList<>();

                if(snapshot.exists())
                    for(DataSnapshot child : snapshot.getChildren()) {
                        Status status = new Status();
                        status.setId(child.getKey());
                        status.setTitle(child.getValue(String.class));
                        statuses.add(status);
                    }

                listener.onSuccess(statuses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });

    }

    public void fetchUsers(FetchUsersListener listener) {
        database.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<User> users = new ArrayList<>();

                if(snapshot.exists())
                    for(DataSnapshot dsUser : snapshot.getChildren()) {
                        User user = new User();
                        user.setUid(dsUser.getKey());
                        user.setName(dsUser.child("name").getValue(String.class));
                        user.setEmail(dsUser.child("email").getValue(String.class));
                        user.setRole(dsUser.child("role").getValue(String.class));
                        users.add(user);
                    }

                listener.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }

    public void fetchAvailability(FetchAvailabilityListener listener) {
        database.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Availability> availabilities = new ArrayList<>();

                if(snapshot.exists())
                    for(DataSnapshot child : snapshot.getChildren())
                        availabilities.add(new Availability(child.getKey(), child.getValue(String.class)));

                listener.onSuccess(availabilities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }

    public void signout() {
        auth.signOut();
    }

//    public void setStatusList() {
//
//        DatabaseReference ref = database.getReference();
//
//        ref.child("status").push().setValue("W drodze do remizy");
//        ref.child("status").push().setValue("Dojazd na miejsce");
//        ref.child("status").push().setValue("W straży");
//        ref.child("status").push().setValue("W gotowości");
//        ref.child("status").push().setValue("Na telefon");
//        ref.child("status").push().setValue("W razie potrzeby dojadę");
//        ref.child("status").push().setValue("Brak dostępności");
//    }




}
