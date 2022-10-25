package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.MainActivity;
import edu.wseiz.remizaosp.databinding.FragmentDashboardBinding;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.DatabaseListener;
import edu.wseiz.remizaosp.tools.Notifier;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Database database;
    private Notifier notifier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        notifier = ((MainActivity) requireActivity()).getNotifier();
        database = ((MainActivity) requireActivity()).getDatabase();

        database.fetchStatuses(new DatabaseListener() {
            @Override
            public void onSuccess() {
                database.fetchUserStatuses(new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        int statusId = database.getUserStatus();
                        if (statusId != -1)
                            binding.txtStatus.setText(database.getStatuses().get(statusId));
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


        binding.btnAcceptEventParticipation.setOnClickListener(v -> {
        });


        return binding.getRoot();
    }


    @Override public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }

}

