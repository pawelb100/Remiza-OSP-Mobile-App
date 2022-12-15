package edu.wseiz.remizaosp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import edu.wseiz.remizaosp.databinding.FragmentAddEventBinding;


public class AddEventFragment extends Fragment {

private FragmentAddEventBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}