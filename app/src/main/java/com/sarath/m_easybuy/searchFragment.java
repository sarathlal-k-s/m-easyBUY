package com.sarath.m_easybuy;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class searchFragment extends Fragment implements itemAdapter.OnListItemClick{

    public searchFragment() {
        // Required empty public constructor
    }

    itemAdapter recycleradapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        Query query = fstore.collection("ads").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<adModel> options = new FirestoreRecyclerOptions.Builder<adModel>()
                .setQuery(query,adModel.class).build();


        recycleradapter = new itemAdapter(options,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recycleradapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recycleradapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recycleradapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot) {
        adModel adModel = documentSnapshot.toObject(adModel.class);
        Intent intent = new Intent(getActivity(),AdPageActivity.class).putExtra("data",adModel);
        startActivity(intent);
    }
}
