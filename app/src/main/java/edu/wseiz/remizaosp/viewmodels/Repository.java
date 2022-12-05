package edu.wseiz.remizaosp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.wseiz.remizaosp.listeners.AddEventListener;
import edu.wseiz.remizaosp.listeners.FetchEventListener;
import edu.wseiz.remizaosp.listeners.FetchEventsListener;
import edu.wseiz.remizaosp.listeners.FetchParticipationListListener;
import edu.wseiz.remizaosp.listeners.FetchParticipationListener;
import edu.wseiz.remizaosp.listeners.FetchStatusByIdListener;
import edu.wseiz.remizaosp.listeners.FetchStatusListListener;
import edu.wseiz.remizaosp.listeners.FetchUserStatusIdListener;
import edu.wseiz.remizaosp.listeners.FetchUsersListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Address;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Participation;
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
            database.getReference().child("users").child(auth.getUid()).child("statusId").setValue(StatusId).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                    listener.onSuccess();
                else
                    listener.onFailed();
            });
        }
        else
            listener.onFailed();
    }

    public void updateUser(User user, UpdateListener listener) {

        database.getReference().child("users").child(user.getUid()).setValue(user).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }

    public void addEvent(Event event, AddEventListener listener) {

        DatabaseReference ref = database.getReference().child("events");
        String key = ref.push().getKey();
        event.setUid(key);
        ref.child(key).setValue(event).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                listener.onSuccess(key);
            else
                listener.onFailed();
        });
    }

    public void updateEvent(Event event, UpdateListener listener) {

        database.getReference().child("events").child(event.getUid()).setValue(event).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }


    public void fetchUserStatusId(FetchUserStatusIdListener listener) {

        if (auth.getUid()!=null) {
            database.getReference().child("users").child(auth.getUid()).child("statusId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String statusId = snapshot.getValue(String.class);
                        listener.onSuccess(statusId);
                    }
                    else
                        listener.onNoData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailed();
                }
            });
        }
        else
            listener.onFailed();

    }

    public void fetchStatusById(String statusId, FetchStatusByIdListener listener) {

        database.getReference().child("status").child(statusId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Status status = snapshot.getValue(Status.class);
                    listener.onSuccess(status);
                }
                else
                    listener.onNoData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }

    public void fetchStatusList(FetchStatusListListener listener) {

        database.getReference().child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Status> statuses = new ArrayList<>();

                if(snapshot.exists())
                    for(DataSnapshot child : snapshot.getChildren()) {
                        Status status = child.getValue(Status.class);
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
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
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

    public void fetchEvents(FetchEventsListener listener) {

        Query query = database.getReference().child("events").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Event> events = new ArrayList<>();

                if(snapshot.exists()) {
                    for (DataSnapshot eventData : snapshot.getChildren()) {
                        Event event = eventData.getValue(Event.class);

                        List<Participation> participationList = new ArrayList<>();
                        for (DataSnapshot participationChild : eventData.child("participation").getChildren())
                            participationList.add(participationChild.getValue(Participation.class));

                        event.setParticipationList(participationList);
                        events.add(event);
                    }
                }

                Collections.reverse(events);
                listener.onSuccess(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }

    public void fetchLastEvent(FetchEventListener listener) {

        Query query = database.getReference().child("events").orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Event> events = new ArrayList<>();

                if(dataSnapshot.exists()) {
                    for(DataSnapshot eventData : dataSnapshot.getChildren()) {
                        Event event = eventData.getValue(Event.class);

                        List<Participation> participationList = new ArrayList<>();
                        for(DataSnapshot participationChild : eventData.child("participation").getChildren())
                            participationList.add(participationChild.getValue(Participation.class));

                        event.setParticipationList(participationList);
                        events.add(event);
                    }

                    listener.onSuccess(events.get(0));
                }
                else
                    listener.onNoData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed();
            }
        });

    }


    public void updateParticipation(Event event, boolean isParticipant, UpdateListener listener) {

        Participation participation = new Participation(auth.getUid(), isParticipant);

        database.getReference().child("events").child(event.getUid()).child("participation").child(participation.getUserId()).setValue(participation).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }

    public void fetchParticipation(Event event, FetchParticipationListener listener) {

        if (auth.getUid()!=null) {
            database.getReference().child("events").child(event.getUid()).child("participation").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Participation participation = snapshot.getValue(Participation.class);
                        listener.onSuccess(participation);
                    } else
                        listener.onNoData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailed();
                }
            });
        }
        else
            listener.onFailed();
    }

    public void fetchParticipationList(Event event, FetchParticipationListListener listener) {

        database.getReference().child("events").child(event.getUid()).child("participation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Participation> participationList = new ArrayList<>();

                if(snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Participation participation = child.getValue(Participation.class);
                        participationList.add(participation);
                    }
                    listener.onSuccess(participationList);
                }
                else
                    listener.onNoData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }


    public void signOut() {
        auth.signOut();
    }

    public void setDefaultStatusList() {

        DatabaseReference ref = database.getReference().child("status");

        String[] str = {"W drodze do remizy", "Dojazd na miejsce","W straży", "W gotowości", "Na telefon", "W razie potrzeby dojadę", "Brak dostępności"};

        for (int i=0; i<str.length; ++i) {
            String key = ref.push().getKey();
            Status status = new Status(key, str[i]);
            ref.child(key).setValue(status);
        }
    }

    public Event createNewEvent(String title, String description, Address address) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription((description));
        event.setTimestamp(System.currentTimeMillis());
        event.setAddress(address);
        return event;
    }




}
