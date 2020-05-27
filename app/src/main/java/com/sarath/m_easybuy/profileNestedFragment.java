package com.sarath.m_easybuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileNestedFragment extends Fragment {
    public profileNestedFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nestedfragment_profile, container, false);

        view.findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        TextView textViewUsername;
        TextView textViewEmail;
        TextView textViewYourAds;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();

            textViewUsername = view.findViewById(R.id.textViewUsername);
            textViewUsername.setText(name);

            textViewEmail = view.findViewById(R.id.textViewEmail);
            textViewEmail.setText(email);
        }

        textViewYourAds = view.findViewById(R.id.textViewYourAds);
        textViewYourAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("here").replace(R.id.fragmentContainerProfileFragment,new myadsNestedFragment()).commit();
            }
        });

        return view;
    }
    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
