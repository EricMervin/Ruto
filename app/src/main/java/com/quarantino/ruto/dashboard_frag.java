package com.quarantino.ruto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;

import java.util.ArrayList;

public class dashboard_frag extends Fragment {

    RecyclerView nearbyPlacesRecycler;
    RecyclerView.Adapter recyclerAdapter;

    public dashboard_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        nearbyPlacesRecycler = v.findViewById(R.id.nearbyPlaces);
        nearbyPlacesRecycler();

        return v;
    }

    private void nearbyPlacesRecycler() {
        nearbyPlacesRecycler.setHasFixedSize(true);
        nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<NearbyPlacesHelperClass> nearbyPlaces = new ArrayList<>();

        nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg, "Hello"));
        nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.illustration_1, "Title2"));

        recyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces);
        nearbyPlacesRecycler.setAdapter(recyclerAdapter);
    }
}
