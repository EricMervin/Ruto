package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class HistoryPlacesAdapter extends RecyclerView.Adapter<HistoryPlacesAdapter.NearbyPlacesViewHolder>{

    private ArrayList<NearbyPlacesHelperClass> nearbyPlaces;
    private OnHistoryPlaceListener onNearbyPlaceListener;

    private Context context;

    public HistoryPlacesAdapter(ArrayList<NearbyPlacesHelperClass> nearbyPlaces, OnHistoryPlaceListener onNearbyPlaceListener) {
        this.nearbyPlaces = nearbyPlaces;
        this.onNearbyPlaceListener = onNearbyPlaceListener;
    }

    @NonNull
    @Override
    public NearbyPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_places_card, parent, false);

        return new NearbyPlacesViewHolder(view, onNearbyPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlacesViewHolder holder, int position) {
        NearbyPlacesHelperClass nearbyPlacesHelperClass = nearbyPlaces.get(position);

        holder.placePhoto.setImageBitmap(nearbyPlacesHelperClass.getImageOfPlace());
        holder.placeName.setText(nearbyPlacesHelperClass.getNameOfPlace());
        holder.placeRatingBar.setRating(nearbyPlacesHelperClass.getRating());
    }

    @Override
    public int getItemCount() {
        return nearbyPlaces.size();
    }

    public static class NearbyPlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View imageOverlay;
        ImageView placePhoto;
        RelativeLayout cardLayout;
        TextView placeName;
        RatingBar placeRatingBar;
        OnHistoryPlaceListener onHistoryPlaceListener;

        public NearbyPlacesViewHolder(@NonNull View itemView, OnHistoryPlaceListener onNearbyPlaceListener){
            super(itemView);

            //Hooks
            placePhoto = itemView.findViewById(R.id.photoNearby);
            cardLayout = itemView.findViewById(R.id.imageNearby);
            placeName = itemView.findViewById(R.id.titleNearby);
            placeRatingBar = itemView.findViewById(R.id.placeRating);
            imageOverlay = itemView.findViewById(R.id.imageOverlay);

            this.onHistoryPlaceListener = onNearbyPlaceListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHistoryPlaceListener.onHistoryPlaceClick(getAdapterPosition(), placeName, placePhoto, placeRatingBar, imageOverlay);
        }
    }

    public interface OnHistoryPlaceListener {
        void onHistoryPlaceClick(int position, TextView placeName, ImageView placePhoto, RatingBar placeRating, View imageOverlay);
    }
}