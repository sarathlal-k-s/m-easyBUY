package com.sarath.m_easybuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class myadsNestedFragment extends Fragment implements itemAdapter.OnListItemClick{
    public myadsNestedFragment() {

    }

    private itemAdapter myadsRecycleradapter;
    private String userid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nestedfragment_myads, container, false);

        RecyclerView myadsRecyclerView = view.findViewById(R.id.myadsRecyclerView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        Query query = fstore.collection("users").document(userid).collection("userAds").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<adModel> options = new FirestoreRecyclerOptions.Builder<adModel>()
                .setQuery(query,adModel.class).build();

        myadsRecycleradapter = new itemAdapter(options,this,2);
        myadsRecyclerView.setHasFixedSize(true);
        myadsRecyclerView.setAdapter(myadsRecycleradapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myadsRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("dd","started listening");
        myadsRecycleradapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("dd","stopped listening");
        myadsRecycleradapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot) {
        adModel adModel = documentSnapshot.toObject(adModel.class);
        Intent intent = new Intent(getActivity(),AdPageActivity.class).putExtra("data",adModel);
        startActivity(intent);
    }

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStackImmediate("here",0);
    }

}