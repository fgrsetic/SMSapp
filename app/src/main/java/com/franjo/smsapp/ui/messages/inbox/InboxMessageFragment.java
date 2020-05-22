package com.franjo.smsapp.ui.messages.inbox;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.franjo.smsapp.R;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.databinding.FragmentMessagesBinding;
import com.franjo.smsapp.domain.Message;
import com.franjo.smsapp.receiver.HeadlessSmsSendService;
import com.franjo.smsapp.ui.MainActivity;
import com.franjo.smsapp.util.ItemDividerDecoration;
import com.franjo.smsapp.util.Permissions;
import com.wajahatkarim3.roomexplorer.RoomExplorer;


public class InboxMessageFragment extends Fragment {

    private Context context;
    private FragmentMessagesBinding binding;
    private InboxMessageViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(requireActivity()).get(InboxMessageViewModel.class);
        binding.setViewModel(viewModel);

        context.startService(new Intent(context, HeadlessSmsSendService.class));

        showMessages();

        RoomExplorer.show(context, AppDatabase.class, "message local database");

        return binding.getRoot();
    }

    private void showMessages() {
        // If permission not granted -> request user for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Permissions.hasPermissions(context, Permissions.PERMISSIONS)) {
            if (getActivity() != null) {
                ActivityCompat.requestPermissions(getActivity(), Permissions.PERMISSIONS, Permissions.PERMISSION_REQUEST);
            }
        } else {
            setAdapter();
            loadMessagesFromStorage();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMessages();
                Toast.makeText(context, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setAdapter() {
        InboxMessageAdapter adapter = new InboxMessageAdapter(new InboxMessageAdapter.IClickListener() {
            @Override
            public void onClick(Message message) {
                viewModel.toMessageDetailsNavigated(message);
            }

            @Override
            public void onContactIconClicked(Message message) {
                //   viewModel.loadContactDetails(message);
            }
        });

        binding.messagesList.setAdapter(adapter);
        binding.messagesList.setHasFixedSize(true);
        ItemDividerDecoration itemDecorator = new ItemDividerDecoration(context, R.drawable.item_divider);
        binding.messagesList.addItemDecoration(itemDecorator);
    }

    private void loadMessagesFromStorage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS_NAME", 0);
        boolean isLoadedFromStorage = sharedPreferences.getBoolean("FIRST_RUN", false);
        if (!isLoadedFromStorage) {
            // Only load from storage for the first time
            viewModel.loadMessagesFromStorage();

            sharedPreferences = context.getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.apply();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // List item -> to message details
        viewModel.navigateToMessageDetails().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
//                MessagesFragmentDirections.MessageDetailsAction action = MessagesFragmentDirections.messageDetailsAction(message);
//                NavHostFragment.findNavController(this).navigate(action);
                viewModel.onMessageDetailsNavigated();
            }
        });

        // Fab button -> to new Message fragment
        viewModel.navigateToNewMessage().observe(getViewLifecycleOwner(), isFabClicked -> {
            if (isFabClicked) {
                Navigation.findNavController(binding.floatingActionButton).navigate(R.id.new_message_action);
                viewModel.doneNavigationToNewMessage();
            }
        });

        viewModel.navigateToContactDetails().observe(getViewLifecycleOwner(), contactUri -> {
            if (contactUri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(contactUri);
                context.startActivity(intent);
                viewModel.doneNavigationToContactDetails();
            }
        });

    }


//    public static boolean showFullText() {
//       // Getting the value of the full sms checkbox preference
//       return sharedPreferences.getBoolean(FULL_SMS, true);
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_main) {
            onSearchClicked(item);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void onSearchClicked(MenuItem item) {
        NavHostFragment.findNavController(this).navigate(R.id.search_messages_action);
    }
}





