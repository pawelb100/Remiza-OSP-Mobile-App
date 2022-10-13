package edu.wseiz.remizaosp.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import edu.wseiz.remizaosp.databinding.FragmentDashboardBinding;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.DatabaseListener;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FirebaseAuth fAuth;
    private Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();

        database = new Database();

        if (user == null)
        {
            //Toast.makeText(getActivity(), user.getEmail(), Toast.LENGTH_SHORT).show();
            //Navigation.findNavController().navigate(R.id.action_hideoutView_to_townView);

        }

        binding.btnChangeStatus.setOnClickListener(v -> {

        });



        binding.btnAcceptEventParticipation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.fetchStatuses(new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println(database.getStatuses());
                    }

                    @Override
                    public void onFailed(DatabaseError databaseError) {
                    }

                });



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
            }
        });

        return binding.getRoot();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}