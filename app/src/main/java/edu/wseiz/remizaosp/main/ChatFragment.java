package edu.wseiz.remizaosp.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wseiz.remizaosp.databinding.FragmentChatBinding;
import edu.wseiz.remizaosp.listeners.AddEventListener;
import edu.wseiz.remizaosp.models.Address;
import edu.wseiz.remizaosp.models.Event;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        repository = viewModelProvider.get(Repository.class);


//        repository.addEvent(repository.createNewEvent("Powalone drzewo", "Spadło drzewo na ulice", new Address("Olszewska 12", "Warszawa Mokotów")), new AddEventListener() {
//            @Override
//            public void onSuccess(String generatedId) {
//
//            }
//
//            @Override
//            public void onFailed() {
//
//            }
//        });


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}