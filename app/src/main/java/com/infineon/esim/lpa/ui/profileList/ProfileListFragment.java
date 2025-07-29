/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.profileList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.dtos.profile.ProfileList;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.ui.dialog.ConfirmationDialog;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.util.Log;

public class ProfileListFragment extends Fragment {
    private static final String TAG = ProfileListFragment.class.getName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listViewSelectedProfile;
    private ListView listViewAvailableProfile;

    private ProfileListViewModel viewModel;

    private ProfileList profileList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileListViewModel.class);
        viewModel.getProfileListLiveData().observe(getViewLifecycleOwner(), profileListObserver);
        viewModel.getActionStatusLiveData().observe(getViewLifecycleOwner(), actionStatusObserver);

        // Set Swipe Refresh Layout
        swipeRefreshLayout = view.findViewById(R.id.profile_list_container);
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshProfileList());

        listViewSelectedProfile = view.findViewById(R.id.list_selected_profile);
        listViewSelectedProfile.setOnItemClickListener(null);
        listViewAvailableProfile = view.findViewById(R.id.list_available_profiles);
        listViewAvailableProfile.setOnItemClickListener(availableProfilesOnItemClickListener);
    }

    public void setEnabledProfile() {
        ProfileListAdapter adapter = new ProfileListAdapter(
                getContext(),
                R.layout.profile_item,
                profileList.getEnabledProfile());
        // Bind data to the ListView
        listViewSelectedProfile.setAdapter(adapter);
    }

    public void setAvailableProfiles() {
        // Set ListAdapter to convert from ArrayList<ProfileMetadata> to View
        ProfileListAdapter adapter = new ProfileListAdapter(
                getContext(),
                R.layout.profile_item,
                profileList.getDisabledProfiles());

        // Bind data to the ListView
        listViewAvailableProfile.setAdapter(adapter);
    }

    final Observer<ProfileList> profileListObserver = profileList -> {
        Log.debug(TAG, "Observed change in profile list.");

        this.profileList = profileList;
        setEnabledProfile();
        setAvailableProfiles();
    };

    final AdapterView.OnItemClickListener availableProfilesOnItemClickListener = (parent, view, position, id) -> {

        ProfileMetadata currentEnabledProfile = null;
        if ((profileList.getEnabledProfile() != null) && (profileList.getEnabledProfile().size() > 0)) {
            currentEnabledProfile = profileList.getEnabledProfile().get(0);
        }

        if (profileList.getDisabledProfiles() != null) {
            ProfileMetadata newEnabledProfile = profileList.getDisabledProfiles().get(position);

            ConfirmationDialog.showEnableProfileConfirmationDialog(
                    getActivity(),
                    currentEnabledProfile,
                    newEnabledProfile);
        }
    };


    final Observer<AsyncActionStatus> actionStatusObserver = actionStatus -> {
        Log.debug(TAG, "Observed that action status changed: " + actionStatus.getActionStatus());

        switch(actionStatus.getActionStatus()) {
            case GET_PROFILE_LIST_STARTED:
                // Start showing the refreshing symbol ("sand clock")
                swipeRefreshLayout.setRefreshing(true);
                break;
            case GET_PROFILE_LIST_FINISHED:
                // Stop showing the refreshing symbol ("sand clock")
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    };
}
