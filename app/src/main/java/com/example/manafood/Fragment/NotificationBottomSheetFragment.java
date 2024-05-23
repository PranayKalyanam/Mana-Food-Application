package com.example.manafood.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.manafood.Adapter.NotificationAdapter;
import com.example.manafood.R;
import com.example.manafood.databinding.FragmentNotificationBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class NotificationBottomSheetFragment extends BottomSheetDialogFragment {

    FragmentNotificationBottomSheetBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBottomSheetBinding.inflate(inflater, container, false);

        List<String> notification = new ArrayList<>();
        notification.add("Your order has been Canceled Successfully");
        notification.add("Order has been taken by the driver");
        notification.add("Congrats Your Order Placed");

        List<Integer> notificationImages = new ArrayList<>();
        notificationImages.add(R.drawable.sademoji);
        notificationImages.add(R.drawable.truck);
        notificationImages.add(R.drawable.successlogo);

        NotificationAdapter adapter = new NotificationAdapter(notification, notificationImages);
        binding.notificationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.notificationRecyclerView.setAdapter(adapter);


        return binding.getRoot();
    }
}



