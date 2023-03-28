package edu.wseiz.remizaosp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.onesignal.OneSignal;

import org.json.JSONObject;

import edu.wseiz.remizaosp.R;
import edu.wseiz.remizaosp.databinding.ActivityMainBinding;
import edu.wseiz.remizaosp.listeners.FetchCurrentUserListener;
import edu.wseiz.remizaosp.models.User;
import edu.wseiz.remizaosp.viewmodels.Repository;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private ActivityMainBinding binding;

    private Repository repository;

    private static final String ONESIGNAL_APP_ID = "a5ab8de1-586c-4d80-a15f-60eb0111b4ad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.topAppBar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.dashboardFragment, R.id.eventsFragment ,R.id.usersFragment, R.id.chatFragment, R.id.settingsFragment)
                .setOpenableLayout(binding.DrawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        repository = viewModelProvider.get(Repository.class);

        repository.singleFetchCurrentUser(new FetchCurrentUserListener() {
            @Override
            public void onSuccess(User user) {
                View headView = binding.navigationView.getHeaderView(0);
                TextView name = headView.findViewById(R.id.name);
                TextView email = headView.findViewById(R.id.email);
                ImageView icon = headView.findViewById(R.id.icon);

                name.setText(user.getName());
                email.setText(user.getEmail());

                switch (user.getRole()) {
                    case Rescuer:
                        icon.setBackgroundResource(R.drawable.ic_baseline_person_24);
                        break;
                    case Driver:
                        icon.setBackgroundResource(R.drawable.ic_baseline_drive_eta_24);
                        break;
                    case Officer:
                        icon.setBackgroundResource(R.drawable.ic_baseline_star_24);
                        break;
                    default:
                }
            }

            @Override
            public void onFailed() {
            }
        });

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.promptForPushNotifications();

    }



    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        return NavigationUI.navigateUp(navController,appBarConfiguration) || super.onSupportNavigateUp();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}