/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */
package com.infineon.esim.lpa.ui.profileList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.ui.generic.ProfileIcons;
import com.infineon.esim.lpa.ui.profileDetails.ProfileDetailsActivity;
import com.infineon.esim.util.Log;

import java.util.List;

final public class ProfileListAdapter extends ArrayAdapter<ProfileMetadata> {
    private static final String TAG = ProfileListAdapter.class.getName();

    private final List<ProfileMetadata> profileList;
    private final int resource;

    public ProfileListAdapter(Context context, int resource, List<ProfileMetadata> profiles) {
        super(context, resource, profiles);
        this.resource = resource;
        this.profileList = profiles;

        // Log profile list
        logProfileList();
    }

    private void logProfileList() {

        for(int i = 0; i < profileList.size(); i++) {
            Log.verbose(TAG, "position " + i);
            Log.verbose(TAG, "status " + profileList.get(i).getState());
            Log.verbose(TAG, "nickname " + profileList.get(i).getNickname());
            Log.verbose(TAG, "name " + profileList.get(i).getName());
            Log.verbose(TAG, "iccid " + profileList.get(i).getIccid());
        }
    }

    // Hold views of the ListView to improve its scrolling performance
    static class ListViewElementViewHolder {
        public TextView provider; // provide name
        public TextView status; // profile status (enabled/disabled)
        public ImageView profileIcon; // profile icon
        public ImageView preferenceIcon; // preference icon (gear)
        public int position;
    }

    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Log.verbose(TAG, "Getting view: position " + position +".");

        // Check if old view can be reused (!=null) or has to be created
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }

        // Create new ListViewElementViewHolder populate with the current element
        ListViewElementViewHolder listViewElementViewHolder = new ListViewElementViewHolder();
        listViewElementViewHolder.position = position;
        listViewElementViewHolder.status = convertView.findViewById(R.id.text_profile_list_item_status);
        listViewElementViewHolder.provider = convertView.findViewById(R.id.text_provider);
        listViewElementViewHolder.profileIcon = convertView.findViewById(R.id.image_profile_icon);
        listViewElementViewHolder.preferenceIcon = convertView.findViewById(R.id.image_profile_details_icon);

        // Set convertView to new content according to position in list
        listViewElementViewHolder.status.setText(profileList.get(position).getState());
        listViewElementViewHolder.provider.setText(profileList.get(position).getNickname());

        if(profileList.get(position).getIcon() != null) {
            listViewElementViewHolder.profileIcon.setImageIcon(profileList.get(position).getIcon());
        } else {
            listViewElementViewHolder.profileIcon.setImageResource(ProfileIcons.lookupProfileImage(profileList.get(position).getName()));
        }

        // Set tag for enabling settings menu click in onClick method
        convertView.setTag(listViewElementViewHolder);
        listViewElementViewHolder.status.setTag(listViewElementViewHolder);
        listViewElementViewHolder.provider.setTag(listViewElementViewHolder);
        listViewElementViewHolder.profileIcon.setTag(listViewElementViewHolder);
        listViewElementViewHolder.preferenceIcon.setTag(listViewElementViewHolder);
        listViewElementViewHolder.preferenceIcon.setOnClickListener(profileImageOnClickListener);

        return convertView;
    }

    final View.OnClickListener profileImageOnClickListener = view -> {
        // Get profile metadata from (image)View via tag
        ImageView imageView = (ImageView) view;
        ListViewElementViewHolder listViewElementViewHolder = (ListViewElementViewHolder) imageView.getTag();
        ProfileMetadata profileMetadata = super.getItem(listViewElementViewHolder.position);

        // Send profile metadata to the new intent
        Context context = super.getContext();
        Intent intent = new Intent(context, ProfileDetailsActivity.class);
        intent.putExtra(Application.INTENT_EXTRA_PROFILE_METADATA, profileMetadata);
        context.startActivity(intent);
    };
}

