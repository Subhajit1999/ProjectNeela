package com.subhajitkar.commercial.project_neela.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.subhajitkar.commercial.project_neela.R;

public class ConversationsFragment extends Fragment {
    private static final String TAG = "ConversationsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }
}