package edu.wseiz.remizaosp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import edu.wseiz.remizaosp.listeners.AddEventListener;
import edu.wseiz.remizaosp.listeners.FetchCurrentUserListener;
import edu.wseiz.remizaosp.listeners.FetchEventListener;
import edu.wseiz.remizaosp.listeners.FetchEventsListener;
import edu.wseiz.remizaosp.listeners.FetchMessagesListener;
import edu.wseiz.remizaosp.listeners.FetchStatusByIdListener;
import edu.wseiz.remizaosp.listeners.FetchStatusListListener;
import edu.wseiz.remizaosp.listeners.FetchUserRoleListener;
import edu.wseiz.remizaosp.listeners.FetchUserStatusIdListener;
import edu.wseiz.remizaosp.listeners.FetchUsersByParticipationsListener;
import edu.wseiz.remizaosp.listeners.FetchUsersListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Message;
import edu.wseiz.remizaosp.models.Participation;
import edu.wseiz.remizaosp.models.Role;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.models.User;

public class Repository extends AndroidViewModel {

    private final FirebaseDatabase database;
    private final FirebaseAuth auth;

    private ValueEventListener userStatusIdListener;
    private ValueEventListener statusListListener;
    private ValueEventListener eventsListener;
    private ValueEventListener eventListener;
    private ValueEventListener ongoingEventsListener;
    private ValueEventListener usersListener;
    private ValueEventListener chatListener;


    public Repository(@NonNull Application application) {
        super(application);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        database.getReference().child("users").child(Objects.requireNonNull(auth.getUid())).keepSynced(true);
        database.getReference().child("status").keepSynced(true);

    }

    public String getUserId() {
        return auth.getUid();
    }


    public void updateStatus(String StatusId, UpdateListener listener) {
        if (auth.getUid() != null) {
            database.getReference().child("users").child(auth.getUid()).child("statusId").setValue(StatusId).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    listener.onSuccess();
                else
                    listener.onFailed();
            });
        } else
            listener.onFailed();
    }

    public void updateUser(User user, UpdateListener listener) {

        database.getReference().child("users").child(user.getUid()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful())
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
            if (task.isSuccessful())
                listener.onSuccess(key);
            else
                listener.onFailed();
        });
    }

    public void deleteEvent(String eventId, UpdateListener listener) {

        database.getReference().child("events").child(eventId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }

    public void updateEvent(Event currentEvent, Event newEvent, UpdateListener listener) {


        database.getReference().child("events").child(currentEvent.getUid()).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                if (!(currentEvent.getTitle().equals(newEvent.getTitle())))
                    currentData.child("title").setValue(newEvent.getTitle());

                if (!(currentEvent.getDescription().equals(newEvent.getDescription())))
                    currentData.child("description").setValue(newEvent.getDescription());

                if (!(currentEvent.getAddress().getStreet().equals(newEvent.getAddress().getStreet())))
                    currentData.child("address").child("street").setValue(newEvent.getAddress().getStreet());

                if (!(currentEvent.getAddress().getRegion().equals(newEvent.getAddress().getRegion())))
                    currentData.child("address").child("region").setValue(newEvent.getAddress().getRegion());

                if (currentEvent.isOngoing()!=newEvent.isOngoing())
                    currentData.child("ongoing").setValue(newEvent.isOngoing());

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed)
                    listener.onSuccess();
                else
                    listener.onFailed();
            }
        });
    }


    public void fetchUserStatusId(FetchUserStatusIdListener listener) {

        if (auth.getUid() != null) {

            userStatusIdListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String statusId = snapshot.getValue(String.class);
                        listener.onSuccess(statusId);
                    } else
                        listener.onNoData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailed();
                }
            };

            database.getReference().child("users").child(auth.getUid()).child("statusId").addValueEventListener(userStatusIdListener);
        } else
            listener.onFailed();

    }

    public void fetchStatusById(String statusId, FetchStatusByIdListener listener) {

        database.getReference().child("status").child(statusId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Status status = snapshot.getValue(Status.class);
                    listener.onSuccess(status);
                } else
                    listener.onNoData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }

    public void fetchStatusList(FetchStatusListListener listener) {

        statusListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Status> statuses = new ArrayList<>();

                if (snapshot.exists())
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Status status = child.getValue(Status.class);
                        statuses.add(status);
                    }

                listener.onSuccess(statuses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        };

        database.getReference().child("status").addValueEventListener(statusListListener);

    }

    public void fetchUsers(FetchUsersListener listener) {

        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<User> users = new ArrayList<>();

                if (snapshot.exists())
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        users.add(user);
                    }

                listener.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        };

        database.getReference().child("users").addValueEventListener(usersListener);

    }

    public void fetchUserRole(FetchUserRoleListener listener) {

        if (auth.getUid() != null)
            database.getReference().child("users").child(auth.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Role role = snapshot.getValue(Role.class);
                        listener.onSuccess(role);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onFailed();
                }
            });
        else
            listener.onFailed();
    }

    public void fetchEvents(FetchEventsListener listener) {

        Query query = database.getReference().child("events").orderByKey();

        eventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Event> events = new ArrayList<>();

                if (snapshot.exists()) {
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
        };

        query.addValueEventListener(eventsListener);
    }

    public void fetchOngoingEvents(FetchEventsListener listener) {

        Query query = database.getReference().child("events").orderByKey();
        ongoingEventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Event> events = new ArrayList<>();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventData : dataSnapshot.getChildren()) {

                        if (Boolean.TRUE.equals(eventData.child("ongoing").getValue(Boolean.class))) {
                            Event event = eventData.getValue(Event.class);

                            List<Participation> participationList = new ArrayList<>();
                            for (DataSnapshot participationChild : eventData.child("participation").getChildren())
                                participationList.add(participationChild.getValue(Participation.class));

                            event.setParticipationList(participationList);
                            events.add(event);
                        }
                    }

                    events.sort(Comparator.comparing(Event::getTimestamp).reversed());
                    listener.onSuccess(events);
                } else {
                    //listener.onNoData();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed();
            }
        };

        query.addValueEventListener(ongoingEventsListener);

    }

    public void fetchEventById(String eventId, FetchEventListener listener) {

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Event event = snapshot.getValue(Event.class);

                    List<Participation> participationList = new ArrayList<>();
                    for (DataSnapshot participationChild : snapshot.child("participation").getChildren())
                        participationList.add(participationChild.getValue(Participation.class));

                    event.setParticipationList(participationList);

                    listener.onSuccess(event);
                } else
                    listener.onNoData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        };

        database.getReference().child("events").child(eventId).addValueEventListener(eventListener);

    }

    public void singleFetchCurrentUser(FetchCurrentUserListener listener) {
        database.getReference().child("users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    listener.onSuccess(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });

    }

    public void singleFetchEventById(String eventId, FetchEventListener listener) {
        database.getReference().child("events").child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Event event = snapshot.getValue(Event.class);

                    listener.onSuccess(event);
                } else
                    listener.onNoData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        });
    }


    public void updateParticipation(Event event, boolean isParticipant, UpdateListener listener) {

        Participation participation = new Participation(auth.getUid(), isParticipant);

        database.getReference().child("events").child(event.getUid()).child("participation").child(participation.getUserId()).setValue(participation).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }


    public void fetchUsersByParticipations(List<Participation> participationList, FetchUsersByParticipationsListener listener) {

        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<User> accepted = new ArrayList<>();
                List<User> rejected = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);

                        for (Participation participation : participationList) {
                            if (participation.getUserId().equals(user.getUid()))
                            {
                                if (participation.isParticipating())
                                    accepted.add(user);
                                else
                                    rejected.add(user);
                            }
                        }
                    }
                }
                listener.onSuccess(accepted, rejected);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        };

        database.getReference().child("users").addValueEventListener(usersListener);

    }

    public void fetchMessages(FetchMessagesListener listener) {

        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Message> messageList = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Message message = child.getValue(Message.class);
                        messageList.add(message);
                        }
                    }

                listener.onSuccess(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailed();
            }
        };

        database.getReference().child("chat").addValueEventListener(chatListener);

    }

    public void sendMessage(Message message, UpdateListener listener) {

        DatabaseReference ref = database.getReference().child("chat");
        String key = ref.push().getKey();
        message.setId(key);
        ref.child(key).setValue(message).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }

    public void deleteMessage(String messageId, UpdateListener listener) {

        database.getReference().child("chat").child(messageId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }




    public void signOut() {
        database.getReference().child("users").child(Objects.requireNonNull(auth.getUid())).keepSynced(false);
        auth.signOut();
    }

    public void updateUserName(String name, UpdateListener listener) {
        database.getReference().child("users").child(auth.getUid()).child("name").setValue(name).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listener.onSuccess();
            else
                listener.onFailed();
        });
    }


    public void setDefaultStatusList() {

        DatabaseReference ref = database.getReference().child("status");

        String[] str = {"W drodze do remizy", "Dojazd na miejsce", "W straży", "W gotowości", "Na telefon", "W razie potrzeby dojadę", "Brak dostępności"};

        for (int i = 0; i < str.length; ++i) {
            String key = ref.push().getKey();
            Status status = new Status(key, str[i]);
            ref.child(key).setValue(status);
        }
    }

    public void removeChatListener() {
        if (chatListener!=null) {
            database.getReference().child("chat").removeEventListener(chatListener);
            chatListener = null;
        }
    }

    public void removeEventsListener() {
        if (eventsListener!=null) {
            database.getReference().child("events").removeEventListener(eventsListener);
            eventsListener = null;
        }
    }

    public void removeEventListener(String eventId) {
        if (eventListener!=null) {
            database.getReference().child("events").child(eventId).removeEventListener(eventListener);
            eventListener = null;
        }
    }


    public void removeOngoingEventsListener() {
        if (ongoingEventsListener!=null) {
            database.getReference().child("events").removeEventListener(ongoingEventsListener);
            ongoingEventsListener = null;
        }
    }

    public void removeStatusListListener() {
        if (statusListListener!=null) {
            database.getReference().child("status").removeEventListener(statusListListener);
            statusListListener = null;
        }
    }

    public void removeUserStatusIdListener() {
        if ((userStatusIdListener!=null)&&(auth.getUid() != null)) {
            database.getReference().child("users").child(auth.getUid()).child("statusId").removeEventListener(userStatusIdListener);
            userStatusIdListener = null;
        }
    }

    public void removeUsersListener() {
        if (usersListener!=null) {
            database.getReference().child("users").removeEventListener(usersListener);
            usersListener = null;
        }
    }





}
