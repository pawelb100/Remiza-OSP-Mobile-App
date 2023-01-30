package edu.wseiz.remizaosp.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.adapters.PendingEventListAdapter;
import edu.wseiz.remizaosp.databinding.FragmentDashboardBinding;
import edu.wseiz.remizaosp.dialogs.DialogStatusSelect;
import edu.wseiz.remizaosp.interfaces.OnItemListClick;
import edu.wseiz.remizaosp.interfaces.OnPendingEventClick;
import edu.wseiz.remizaosp.listeners.FetchEventsListener;
import edu.wseiz.remizaosp.listeners.FetchStatusByIdListener;
import edu.wseiz.remizaosp.listeners.FetchStatusListListener;
import edu.wseiz.remizaosp.listeners.FetchUserRoleListener;
import edu.wseiz.remizaosp.listeners.FetchUserStatusIdListener;
import edu.wseiz.remizaosp.listeners.UpdateListener;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.models.Role;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.viewmodels.Repository;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Repository repository;

    private Status currentStatus;
    private List<Status> statuses;

    private PendingEventListAdapter adapter;

    private Role userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        adapter = null;
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
                        public void onSuccess() {info("Zaktualizowano status");}

                        @Override
                        public void onFailed() {handleFail();}
                    });

                    currentStatus = status;
                    binding.txtStatus.setText(status.getTitle());
                    dialog.dismiss();
                }

                @Override
                public void onItemLongClick(Object item, View view) {}
            });

            dialog.show();
        });

        return binding.getRoot();
    }


    @Override public void onDestroyView () {
        super.onDestroyView();
        repository.removeUserStatusIdListener();
        repository.removeStatusListListener();
        repository.removeOngoingEventsListener();
        binding = null;
    }



    private void loadData() {

        repository.fetchUserRole(new FetchUserRoleListener() {
            @Override
            public void onSuccess(Role role) {
                if (role==Role.Officer) {
                    binding.addFab.setVisibility(View.VISIBLE);
                    binding.addFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_dashboardFragment_to_addEventFragment));
                }
                userRole = role;
            }

            @Override
            public void onFailed() {}
        });


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

        repository.fetchOngoingEvents(new FetchEventsListener() {
            @Override
            public void onSuccess(List<Event> events) {
                setAdapter(events);
            }

            @Override
            public void onFailed() {handleFail();}
        });

    }

    private void setAdapter(List<Event> events) {

        if (adapter==null) {
            adapter = new PendingEventListAdapter(getContext(), events, repository.getUserId(), new OnPendingEventClick() {

                @Override
                public void onClick(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventId", events.get(position).getUid());
                    bundle.putBoolean("editable", userRole == Role.Officer);
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.eventFragment, bundle);
                }

                @Override
                public void onAccept(int position) {
                    repository.updateParticipation(events.get(position), true, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            info("Zaakceptowano");
                        }

                        @Override
                        public void onFailed() {
                            handleFail();
                        }
                    });
                }

                @Override
                public void onReject(int position) {
                    repository.updateParticipation(events.get(position), false, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            info("Zrezygnowano");
                        }

                        @Override
                        public void onFailed() {
                            handleFail();
                        }
                    });
                }
            });
            binding.pager.setAdapter(adapter);
        }
        else
            adapter.updateData(events);

        binding.tvCurrentEvents.setText("Aktualne zdarzenia: " + events.size());
    }


    private void info(String text) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void handleFail() {
        info("Wystąpił problem z połączeniem");
    }


}

