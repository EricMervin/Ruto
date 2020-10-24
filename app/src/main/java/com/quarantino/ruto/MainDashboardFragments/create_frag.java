package com.quarantino.ruto.MainDashboardFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter.SelectedPlacesAdapter;
import com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter.SelectedPlacesHelperClass;
import com.quarantino.ruto.R;

import java.util.ArrayList;

public class create_frag extends Fragment{

    private RecyclerView nearbyPlacesRecycler, selectedPlacesRecycler;
    private RecyclerView.Adapter nearbyPlacesRecyclerAdapter, selectedPlacesRecyclerAdapter;
    private Button addPlaceBtn;

    String[] placesArr = { "Restaurants", "Museums", "Cafe", "Airport", "Library"};

    private ArrayList<SelectedPlacesHelperClass> selectedPlacesList = new ArrayList<>();

    public create_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompletePlace);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_row, placesArr);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setDropDownVerticalOffset(9);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.invalidate();

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autoCompleteTextView.showDropDown();
                return false;
            }
        });


        addPlaceBtn = view.findViewById(R.id.ericBtn);
        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
                selectedPlacesList.add(new SelectedPlacesHelperClass("Lotus Temple", icon1));
                selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
            }
        });

        //Selected Places Recycler View
        selectedPlacesRecycler = view.findViewById(R.id.selectedPlaceRecycler);
        selectedPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedPlacesRecyclerAdapter = new SelectedPlacesAdapter(selectedPlacesList);
        selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
        return view;
    }



}
