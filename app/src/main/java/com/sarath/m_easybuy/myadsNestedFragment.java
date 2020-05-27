package com.sarath.m_easybuy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class myadsNestedFragment extends Fragment implements itemAdapter.OnListItemClick{
    public myadsNestedFragment() {

    }

    private itemAdapter myadsRecycleradapter;
    private String userid;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nestedfragment_myads, container, false);

        RecyclerView myadsRecyclerView = view.findViewById(R.id.myadsRecyclerView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        Query queryUserads = fstore.collection("users").document(userid).collection("userAds").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<adModel> options = new FirestoreRecyclerOptions.Builder<adModel>()
                .setQuery(queryUserads,adModel.class).build();

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
        Log.d("dd","id :"+documentSnapshot.getData());
        adModel adModel = documentSnapshot.toObject(adModel.class);
        Intent intent = new Intent(getActivity(),AdPageActivity.class).putExtra("data",adModel);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(final DocumentSnapshot documentSnapshot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete this ad?");
        builder.setMessage("Are you sure you want to delete this ad?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                documentSnapshot.getReference().delete();

                DocumentReference documentReference = fstore.collection("ads").document(documentSnapshot.getId());
                documentReference.delete();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference("images").child(documentSnapshot.getId());
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Ad Deleted",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStackImmediate("here",0);
    }

}