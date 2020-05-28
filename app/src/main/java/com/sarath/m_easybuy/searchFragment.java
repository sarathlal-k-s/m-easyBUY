package com.sarath.m_easybuy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class searchFragment extends Fragment implements itemAdapter.OnListItemClick{

    public searchFragment() {
        // Required empty public constructor
    }

    private itemAdapter recycleradapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    int lastPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        lastPosition = 0;
        recyclerView = view.findViewById(R.id.recyclerView);
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        Query query = fstore.collection("ads").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<adModel> options = new FirestoreRecyclerOptions.Builder<adModel>()
                .setQuery(query,adModel.class).build();

        recycleradapter = new itemAdapter(options,this,1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recycleradapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recycleradapter.startListening();
        new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                Log.d("shared","on start pos :"+Integer.toString(lastPosition));
                try {
                    recyclerView.smoothScrollToPosition(lastPosition);
                }catch (IllegalArgumentException e){
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        recycleradapter.stopListening();
    }


    @Override
    public void onPause() {
        super.onPause();
        lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        Log.d("shared","On pause last pos :"+Integer.toString(lastPosition));

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot) {
        adModel adModel = documentSnapshot.toObject(adModel.class);
        Intent intent = new Intent(getActivity(),AdPageActivity.class).putExtra("data",adModel);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(DocumentSnapshot documentSnapshot) {

    }
}
