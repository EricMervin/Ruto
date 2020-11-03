package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.content.res.Resources;
import android.util.Log;
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

public class NearbyPlacesItineraryAdapter extends RecyclerView.Adapter<NearbyPlacesItineraryAdapter.NearbyPlacesCreateFragViewHolder> {

    private ArrayList<NearbyPlacesHelperClass> nearbyTypePlaceList;
    private OnNearbyPlaceRouteListener onNearbyPlaceRouteListener;
    Resources context;

    public NearbyPlacesItineraryAdapter(ArrayList<NearbyPlacesHelperClass> nearbyTypePlaceList, OnNearbyPlaceRouteListener onNearbyPlaceRouteListener) {
        this.nearbyTypePlaceList = nearbyTypePlaceList;
        this.onNearbyPlaceRouteListener = onNearbyPlaceRouteListener;
    }

    @NonNull
    @Override
    public NearbyPlacesCreateFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_place_itinerary_card, parent, false);

        context = parent.getContext().getResources();
        return new NearbyPlacesCreateFragViewHolder(view, onNearbyPlaceRouteListener);
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

    public static class NearbyPlacesCreateFragViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView placeName, ratingPlace, openPlace;
        ImageView placePhoto, iconButton;
        FrameLayout addPlaceButton;
        ConstraintLayout addPlaceButtonBackground;
        boolean isAdded = true;
        OnNearbyPlaceRouteListener onNearbyPlaceRouteListener;

        public NearbyPlacesCreateFragViewHolder(@NonNull View itemView, final OnNearbyPlaceRouteListener onNearbyPlaceRouteListener) {
            super(itemView);

            //Hooks
            addPlaceButton = itemView.findViewById(R.id.addButton);
            addPlaceButtonBackground = itemView.findViewById(R.id.addButtonBackground);
            iconButton = itemView.findViewById(R.id.iconButton);
            placeName = itemView.findViewById(R.id.nearbyPlaceName);
            placePhoto = itemView.findViewById(R.id.nearbyPlacePhoto);
            ratingPlace = itemView.findViewById(R.id.ratingOfPlace);
            openPlace = itemView.findViewById(R.id.statusOfPlace);

            this.onNearbyPlaceRouteListener = onNearbyPlaceRouteListener;
            itemView.setOnClickListener(this);

            addPlaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isAdded){
                        addPlaceButtonBackground.setBackgroundResource(R.drawable.circle_button_check);
                        iconButton.setImageResource(R.drawable.tick_icon);
                    }
                    onNearbyPlaceRouteListener.onAddPlaceRouteClick(getAdapterPosition(), isAdded);
                    isAdded = !isAdded;
                }
            });
        }

        @Override
        public void onClick(View view) {
            onNearbyPlaceRouteListener.onNearbyPlaceRouteClick(getAdapterPosition(), placeName);
        }
    }

    public interface OnNearbyPlaceRouteListener{
        void onNearbyPlaceRouteClick(int position, TextView placeName);
        void onAddPlaceRouteClick(int position, boolean isAdded);
    }
}
