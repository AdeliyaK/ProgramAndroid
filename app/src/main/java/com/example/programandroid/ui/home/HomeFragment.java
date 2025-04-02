package com.example.programandroid.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.programandroid.R;
import com.example.programandroid.TokenManager;
import com.example.programandroid.ui.home.HomeViewModel;

public class HomeFragment extends Fragment {

    private TextView welcomeTextView;
    private TextView currentActivitiesTextView;
    private TextView futureActivitiesTextView;
    private HomeViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeTextView = rootView.findViewById(R.id.WelcomeText);
        currentActivitiesTextView = rootView.findViewById(R.id.current_activities_text);
        futureActivitiesTextView = rootView.findViewById(R.id.future_activities_text);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Наблюдаваме за промени в данните и актуализираме UI
        homeViewModel.getUserMessage().observe(getViewLifecycleOwner(), message -> {
            welcomeTextView.setText(message);
        });

        homeViewModel.getCurrentActivities().observe(getViewLifecycleOwner(), activities -> {
            currentActivitiesTextView.setText(activities);
        });

        homeViewModel.getFutureActivities().observe(getViewLifecycleOwner(), activities -> {
            futureActivitiesTextView.setText(activities);
        });

        TokenManager tokenManager = new TokenManager(this.getContext());
        String accessToken = tokenManager.getAccessToken();
        if (accessToken != null) {
            homeViewModel.fetchUserInfo(accessToken);
            homeViewModel.fetchUserActivities(accessToken);
        } else {
            Log.e("HomeScreen", "Access Token липсва!");
        }
        return rootView;
    }
}
