package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.NearbyPlacesViewHolder>{

    ArrayList<NearbyPlacesHelperClass> nearbyPlaces;

    public NearbyPlacesAdapter(ArrayList<NearbyPlacesHelperClass> nearbyPlaces) {
        this.nearbyPlaces = nearbyPlaces;
    }

    @NonNull
    @Override
    public NearbyPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_places_card, parent, false);
        NearbyPlacesViewHolder nearbyPlacesViewHolder = new NearbyPlacesViewHolder(view);
        return nearbyPlacesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlacesViewHolder holder, int position) {
        NearbyPlacesHelperClass nearbyPlacesHelperClass = nearbyPlaces.get(position);

        holder.cardLayout.setBackgroundResource(nearbyPlacesHelperClass.getImage());
        holder.placeName.setText(nearbyPlacesHelperClass.getTitle());
    }

    @Override
    public int getItemCount() {
        return nearbyPlaces.size();
    }

    public static class NearbyPlacesViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout cardLayout;
        TextView placeName;

        public NearbyPlacesViewHolder(@NonNull View itemView){
            super(itemView);

            //Hooks
            cardLayout = itemView.findViewById(R.id.imageNearby);
            placeName = itemView.findViewById(R.id.titleNearby);
        }
    }
}
