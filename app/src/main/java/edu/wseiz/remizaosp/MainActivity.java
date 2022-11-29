package edu.wseiz.remizaosp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import edu.wseiz.remizaosp.databinding.ActivityMainBinding;
import edu.wseiz.remizaosp.tools.Database;
import edu.wseiz.remizaosp.tools.Notifier;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private Database database = new Database();

    public Database getDatabase() {
        return database;
    }

    private Notifier notifier = new Notifier(this);

    public Notifier getNotifier() {
        return notifier;
    }

    private ActivityMainBinding binding;


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

    }



    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        return NavigationUI.navigateUp(navController,appBarConfiguration) || super.onSupportNavigateUp();

    }


}