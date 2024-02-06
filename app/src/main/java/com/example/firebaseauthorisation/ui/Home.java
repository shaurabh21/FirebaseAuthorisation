package com.example.firebaseauthorisation.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.firebaseauthorisation.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements NavController.OnDestinationChangedListener {
    NavHostFragment navHostFragment;
    BottomNavigationView b_view;
    FirebaseAuth mAuth;
    NavController nav;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        b_view = findViewById(R.id.bottom_view);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openDialog();
                return true;
            }
        });

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        assert navHostFragment != null;
        nav = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(b_view, nav);

       nav.addOnDestinationChangedListener(this);
    }

    private void openDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Logout")
                .setMessage("Are you sure")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(Home.this, MainActivity.class));
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();

        }


    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {

        if (navDestination.getId() == R.id.chat) {
            toolbar.setTitle("Chat");
            toolbar.getMenu().findItem(R.id.logout).setVisible(false);
        } else if (navDestination.getId() == R.id.user) {
            toolbar.setTitle("User");
            toolbar.getMenu().findItem(R.id.logout).setVisible(false);
        } else {
            if (navDestination.getId() == R.id.profile) {
                toolbar.setTitle("Profile");
                toolbar.getMenu().findItem(R.id.logout).setVisible(true);
            }


        }

    }


}

