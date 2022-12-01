package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.wseiz.remizaosp.databinding.FragmentDashboardBinding;
import edu.wseiz.remizaosp.dialogs.DialogStatusSelect;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.listeners.FetchStatusByIdListener;
import edu.wseiz.remizaosp.listeners.FetchStatusListListener;
import edu.wseiz.remizaosp.listeners.FetchUserStatusIdListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.viewmodels.Repository;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Repository repository;

    private Status currentStatus;
    private List<Status> statuses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        loadData();


        binding.btnChangeStatus.setOnClickListener(v -> {
            DialogStatusSelect dialog = new DialogStatusSelect(getContext());

            String statusId;
            if (currentStatus!=null)
                statusId = currentStatus.getUid();
            else
                statusId = "";

            dialog.setAdapter(statuses, statusId, new OnItemListClick() {
                @Override
                public void onItemClick(Object item) {
                    Status status = (Status) item;
                    repository.updateStatus(status.getUid(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            currentStatus = status;
                            binding.txtStatus.setText(status.getTitle());
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailed() {handleFail();}
                    });
                }

                @Override
                public void onItemLongClick(Object item, View view) {}
            });

            dialog.show();
        });


        binding.btnAcceptEventParticipation.setOnClickListener(v -> {
        });


        return binding.getRoot();
    }


    @Override public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }

    private void loadData() {

        repository.fetchUserStatusId(new FetchUserStatusIdListener() {
            @Override
            public void onSuccess(String statusId) {
                repository.fetchStatusById(statusId, new FetchStatusByIdListener() {
                    @Override
                    public void onSuccess(Status status) {
                        currentStatus = status;
                        binding.txtStatus.setText(status.getTitle());
                    }

                    @Override
                    public void onNoData() {
                        currentStatus = null;
                        binding.txtStatus.setText("Nieznany status");
                    }

                    @Override
                    public void onFailed() {handleFail();}
                });
            }

            @Override
            public void onNoData() {
                currentStatus = null;
                binding.txtStatus.setText("Nie wybrano");
            }

            @Override
            public void onFailed() {handleFail();}
        });


        repository.fetchStatusList(new FetchStatusListListener() {
            @Override
            public void onSuccess(List<Status> data) {
                statuses = data;
            }

            @Override
            public void onFailed() {handleFail();}
        });

    }

    private void handleFail() {
        Toast.makeText(getContext(), "Wystąpił problem z połączeniem", Toast.LENGTH_LONG).show();
    }

}

