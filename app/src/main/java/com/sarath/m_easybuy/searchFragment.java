package com.sarath.m_easybuy;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class searchFragment extends Fragment {

    public searchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ArrayList<String> animalArray = new ArrayList<>();
        animalArray.add("Calculator");
        animalArray.add("Book");
        animalArray.add("Calculator");
        animalArray.add("Book");
        animalArray.add("Calculator");
        animalArray.add("Book");
        animalArray.add("Calculator");
        animalArray.add("Book");

        itemAdapter recycleradapter = new itemAdapter(getActivity(),animalArray);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setAdapter(recycleradapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
