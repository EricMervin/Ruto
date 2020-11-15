package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.NearbyPlacesCreateFragViewHolder> {

    private ArrayList<NearbyPlacesHelperClass> nearbyTypePlaceList;
    Resources context;

    public ItineraryAdapter(ArrayList<NearbyPlacesHelperClass> nearbyTypePlaceList) {
        this.nearbyTypePlaceList = nearbyTypePlaceList;
    }

    @NonNull
    @Override
    public NearbyPlacesCreateFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_card, parent, false);

        context = parent.getContext().getResources();
        return new NearbyPlacesCreateFragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlacesCreateFragViewHolder holder, int position) {
        NearbyPlacesHelperClass nearbyPlacesHelperClass = nearbyTypePlaceList.get(position);
        holder.placeName.setText(nearbyPlacesHelperClass.getNameOfPlace());
        holder.placePhoto.setImageBitmap(nearbyPlacesHelperClass.getImageOfPlace());
        holder.ratingPlace.setText(String.valueOf(nearbyPlacesHelperClass.getRating()));

        if(nearbyPlacesHelperClass.getOpenStatus().equalsIgnoreCase("true") || nearbyPlacesHelperClass.getOpenStatus().equalsIgnoreCase(" true")){
            holder.openPlace.setText("Open Now");
            holder.openPlace.setTextColor(context.getColor(R.color.checkGreen));
        } else {
            holder.openPlace.setText("Closed");
            holder.openPlace.setTextColor(context.getColor(R.color.customError));
        }
    }

    @Override
    public int getItemCount() {
        return nearbyTypePlaceList.size();
    }

    public static class NearbyPlacesCreateFragViewHolder extends RecyclerView.ViewHolder{
        TextView placeName, ratingPlace, openPlace;
        ImageView placePhoto, iconButton;
        FrameLayout addPlaceButton;
        ConstraintLayout addPlaceButtonBackground;

        public NearbyPlacesCreateFragViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            addPlaceButton = itemView.findViewById(R.id.addButton);
            addPlaceButtonBackground = itemView.findViewById(R.id.addButtonBackground);
            iconButton = itemView.findViewById(R.id.iconButton);
            placeName = itemView.findViewById(R.id.nearbyPlaceName);
            placePhoto = itemView.findViewById(R.id.nearbyPlacePhoto);
            ratingPlace = itemView.findViewById(R.id.ratingOfPlace);
            openPlace = itemView.findViewById(R.id.statusOfPlace);
        }
    }
}
