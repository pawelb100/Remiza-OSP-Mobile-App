package edu.wseiz.remizaosp.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.wseiz.remizaosp.adapters.StatusWithUsersListAdapter;
import edu.wseiz.remizaosp.databinding.FragmentUsersBinding;
import edu.wseiz.remizaosp.listeners.FetchStatusListListener;
import edu.wseiz.remizaosp.listeners.FetchUsersListener;
import edu.wseiz.remizaosp.models.Status;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.viewmodels.Repository;

public class UsersFragment extends Fragment {


    private FragmentUsersBinding binding;

    private Repository repository;

    private StatusWithUsersListAdapter adapter;

    private List<Status> statuses;

    private List<Pair<String, List<User>>> statusTitlesWithUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);

        statusTitlesWithUsers = new ArrayList<>();

        loadData();

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void loadData() {

        repository.fetchStatusList(new FetchStatusListListener() {
            @Override
            public void onSuccess(List<Status> data) {
                statuses = data;

                repository.fetchUsers(new FetchUsersListener() {
                    @Override
                    public void onSuccess(List<User> data) {

                        statusTitlesWithUsers = prepareListToDisplay(statuses, data);

                        if(adapter!=null)
                            adapter.notifyDataSetChanged();
                        else
                            setAdapter();
                    }

                    @Override
                    public void onFailed() {

                    }
                });

            }

            @Override
            public void onFailed() {

            }
        });

    }

    private List<Pair<String, List<User>>> prepareListToDisplay(List<Status> statuses, List<User> users) {

        List<Pair<String, List<User>>> statuesWithUsers = new ArrayList<>();

        for(Status status : statuses)
        {

            String title = status.getTitle();
            List<User> userList = new ArrayList<>();

            for(User user : users)
            {
                if (user.getStatusId()!=null)
                    if(user.getStatusId().equals(status.getUid()))
                    {
                        userList.add(user);
                    }

            }

            if(!(userList.isEmpty())) {
                Pair<String, List<User>> usersInStatus = new Pair<>(title, userList);
                statuesWithUsers.add(usersInStatus);
            }

        }

        return statuesWithUsers;
    }


    private void setAdapter() {
        adapter = new StatusWithUsersListAdapter(getContext(), statusTitlesWithUsers);
        binding.rvStatusesWithUsers.setAdapter(adapter);
        binding.rvStatusesWithUsers.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}