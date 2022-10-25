package edu.wseiz.remizaosp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.DrawerLayout);
        NavigationView navView = findViewById(R.id.navigationView);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.dashboardFragment, R.id.firefighterListFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }



    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        return NavigationUI.navigateUp(navController,appBarConfiguration) || super.onSupportNavigateUp();

    }


}