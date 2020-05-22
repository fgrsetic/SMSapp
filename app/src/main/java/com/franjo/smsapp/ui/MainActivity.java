package com.franjo.smsapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.franjo.smsapp.R;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.databinding.ActivityMainBinding;
import com.wajahatkarim3.roomexplorer.RoomExplorer;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.messages_dest,
                R.id.contacts_dest,
                R.id.favorites_dest)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        bottomNavigationVisibility(navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp();
    }

    // Hide bottom navigation
    private void bottomNavigationVisibility(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.new_message_dest) {
                binding.navView.setVisibility(GONE);
            } else if (destination.getId() == R.id.search_contacts_dest) {
                binding.navView.setVisibility(GONE);
            } else if (destination.getId() == R.id.search_messages_dest) {
                binding.navView.setVisibility(GONE);
            } else {
                binding.navView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu) {
//            showPopupMenu(item);
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//        return false;
//    }

//    public void showPopupMenu(MenuItem item) {
//        final View menuItemView = findViewById(item.getItemId());
//        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
//        MenuInflater inflater = popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.options_popup_menu, popupMenu.getMenu());
//        popupMenu.show();
//    }

}
