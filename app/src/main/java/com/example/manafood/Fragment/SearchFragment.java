package com.example.manafood.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.example.manafood.Adapter.MenuAdapter;
import com.example.manafood.databinding.FragmentSearchBinding;
import com.example.manafood.model.MenuItemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private MenuAdapter adapter;
    private final List<MenuItemModel> originalMenuItems = new ArrayList<>();
    private final List<MenuItemModel> filterMenuItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveMenuItems();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setupSearchView();

        return binding.getRoot();
    }

    private void retrieveMenuItems() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("menu");

        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                originalMenuItems.clear();
                for (DataSnapshot menuItemSnapshot : snapshot.getChildren()) {
                    MenuItemModel menuItem = menuItemSnapshot.getValue(MenuItemModel.class);
                    if (menuItem != null) {
                        originalMenuItems.add(menuItem);
                    }
                }
                showAllMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void showAllMenu() {
        filterMenuItems.clear();
        filterMenuItems.addAll(originalMenuItems);
        setupAdapter();
    }

    private void setupAdapter() {
        if (adapter == null) {
            adapter = new MenuAdapter(filterMenuItems, requireContext());
            binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.menuRecyclerView.setAdapter(adapter);
        } else {
            adapter.notify();
        }
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItems(query);
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return true;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterMenuItems(String query) {
        filterMenuItems.clear();
        for (MenuItemModel item : originalMenuItems) {
            if (item.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                filterMenuItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
