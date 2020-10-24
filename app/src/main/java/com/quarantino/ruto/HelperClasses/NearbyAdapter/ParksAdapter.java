package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class ParksAdapter extends RecyclerView.Adapter<ParksAdapter.ParksViewHolder>{

    private ArrayList<NearbyPlacesHelperClass> nearbyParks;
    private OnParkListener onParkListener;

    private Context context;

    public ParksAdapter(ArrayList<NearbyPlacesHelperClass> nearbyParks, OnParkListener onParkListener) {
        this.nearbyParks = nearbyParks;
        this.onParkListener = onParkListener;
    }

    @NonNull
    @Override
    public ParksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_place_card_alt, parent, false);
//        ParksViewHolder restaurantsViewHolder = new ParksViewHolder(view);

        return new ParksViewHolder(view, onParkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ParksViewHolder holder, int position) {
        NearbyPlacesHelperClass restaurantsHelperClass = nearbyParks.get(position);

        holder.nameOfPlace.setText(restaurantsHelperClass.getNameOfPlace());
        holder.photoOfPlace.setImageBitmap(restaurantsHelperClass.getImageOfPlace());
        holder.ratingOfPlace.setText(String.valueOf(restaurantsHelperClass.getRating()) + " / 5");
    }

    @Override
    public int getItemCount() {
        return nearbyParks.size();
    }

    public static class ParksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View imageOverlay;
        TextView nameOfPlace, ratingOfPlace;
        ImageView photoOfPlace;
        OnParkListener onParkListener;
//        RatingBar ratingOfRestaurant;

        public ParksViewHolder(@NonNull View itemView, OnParkListener onParkListener) {
            super(itemView);

            //Hooks
            nameOfPlace = itemView.findViewById(R.id.placeNameAlt);
            photoOfPlace = itemView.findViewById(R.id.placePhotoAlt);
            ratingOfPlace = itemView.findViewById(R.id.placeRatingAlt);
            imageOverlay = itemView.findViewById(R.id.imageOverlayRes);

            this.onParkListener = onParkListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onParkListener.onParkClick(getAdapterPosition(), nameOfPlace, photoOfPlace, ratingOfPlace, imageOverlay);
        }
    }

    public interface OnParkListener{
        void onParkClick(int position, TextView placeName, ImageView placePhoto, TextView placeRating, View imageOverlay);
    }
}
