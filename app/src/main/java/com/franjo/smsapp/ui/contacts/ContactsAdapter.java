package com.franjo.smsapp.ui.contacts;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.data.Contact;
import com.franjo.smsapp.databinding.ItemContactsListBinding;

/**
 * Created by Franjo on 18.7.2016..
 */
public class ContactsAdapter extends ListAdapter<Contact, ContactsAdapter.ContactsViewHolder> {

    private IClickListener onClickListener;

    private static final DiffUtil.ItemCallback<Contact> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Contact>() {
                @Override
                public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                    return oldItem.equals(newItem);
                }
            };


    ContactsAdapter(IClickListener onClickListener) {
        super(DIFF_CALLBACK);
        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsViewHolder(ItemContactsListBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contact contact = getItem(position);
        holder.bind(contact, onClickListener);
    }

    static class ContactsViewHolder extends RecyclerView.ViewHolder {

        private ItemContactsListBinding binding;

        private ContactsViewHolder(ItemContactsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Contact contact, IClickListener clickListener) {
            binding.setContact(contact);
            binding.setClickListener(clickListener);
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings();
        }

    }

    public interface IClickListener {
        void onClick(Contact contact);

    }
}






















