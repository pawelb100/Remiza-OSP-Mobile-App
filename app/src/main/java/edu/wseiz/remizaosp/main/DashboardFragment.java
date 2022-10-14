package edu.wseiz.remizaosp.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import edu.wseiz.remizaosp.databinding.FragmentDashboardBinding;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.DatabaseListener;
import edu.wseiz.remizaosp.tools.Notifier;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FirebaseAuth fAuth;
    private Database database;
    private Notifier notifier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        database = new Database();
        notifier = new Notifier(getActivity());

        database.fetchStatuses(new DatabaseListener() {
            @Override
            public void onSuccess() {
                database.fetchUserStatus(new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        int statusId = database.getUserStatus(database.getCurrentUserId());
                        if (statusId != -1)
                        {
                            binding.txtStatus.setText(database.getStatuses().get(statusId));
                        }
                        else
                            binding.txtStatus.setText("Brak");


                    }

                    @Override
                    public void onFailed(Exception e) {
                        notifier.alert("Błąd pobierania danych");
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                notifier.alert("Błąd pobierania danych");
            }
        });

        binding.btnChangeStatus.setOnClickListener(v -> {
            notifier.selectListItemDialog("Wybierz status", database.getStatuses().toArray(new String[0]), (dialog, which) -> {
                database.updateStatus(which, new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        binding.txtStatus.setText(database.getStatuses().get(which));
                    }

                    @Override
                    public void onFailed(Exception e) {
                        notifier.alert("Błąd wysyłania danych");
                    }
                });
            });
        });



        binding.btnAcceptEventParticipation.setOnClickListener(view -> {




            /*
            database.fetchUsers(new DatabaseListener() {
                @Override
                public void onSuccess() {
                    for(User user : database.getUsers())
                    {
                        System.out.println(user.getUid() + " " + user.getEmail() + " " + user.getName() + " " + user.getRole());
                    }
                }

                @Override
                public void onFailed(Exception e) {
                }

            });
            */


            /*
            List<String> values = Arrays.asList("W drodze do remizy", "Dojazd na miejsce", "W straży", "W gotowości", "Na telefon", "W razie potrzeby dojadę", "Brak dostępności");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("statuses");

            ref.setValue(values);
            */


            /*
            fAuth.signOut();
            Toast.makeText(getActivity(), "Wylogowano", Toast.LENGTH_SHORT).show();
            Intent activityIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(activityIntent);
            requireActivity().finish();

             */
        });

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}