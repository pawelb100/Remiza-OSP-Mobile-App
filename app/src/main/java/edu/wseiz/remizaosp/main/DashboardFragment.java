package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.MainActivity;
import edu.wseiz.remizaosp.databinding.FragmentDashboardBinding;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.DatabaseListener;
import edu.wseiz.remizaosp.tools.Notifier;
import edu.wseiz.remizaosp.viewmodels.Repository;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        Notifier notifier = new Notifier(getContext());

        repository.getDatabase().fetchStatuses(new DatabaseListener() {
            @Override
            public void onSuccess() {
                repository.getDatabase().fetchUserStatuses(new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        int statusId = repository.getDatabase().getUserStatus();
                        if (statusId != -1)
                            binding.txtStatus.setText(repository.getDatabase().getStatuses().get(statusId));
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
            notifier.selectListItemDialog("Wybierz status", repository.getDatabase().getStatuses().toArray(new String[0]), (dialog, which) -> {
                repository.getDatabase().updateStatus(which, new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        binding.txtStatus.setText(repository.getDatabase().getStatuses().get(which));
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

