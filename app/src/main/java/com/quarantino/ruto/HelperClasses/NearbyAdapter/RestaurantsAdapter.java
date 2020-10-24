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

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>{

    private ArrayList<NearbyPlacesHelperClass> nearbyRestaurants;
    private OnRestaurantListener onRestaurantListener;

    private Context context;

    public RestaurantsAdapter(ArrayList<NearbyPlacesHelperClass> nearbyRestaurants, OnRestaurantListener onRestaurantListener) {
        this.nearbyRestaurants = nearbyRestaurants;
        this.onRestaurantListener = onRestaurantListener;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.nearby_place_card_alt, parent, false);
//        RestaurantsViewHolder restaurantsViewHolder = new RestaurantsViewHolder(view);

        return new RestaurantsViewHolder(view, onRestaurantListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        NearbyPlacesHelperClass restaurantsHelperClass = nearbyRestaurants.get(position);

        holder.nameOfRestaurant.setText(restaurantsHelperClass.getNameOfPlace());
        holder.photoOfRestaurant.setImageBitmap(restaurantsHelperClass.getImageOfPlace());
        holder.ratingOfRestaurant.setText(String.valueOf(restaurantsHelperClass.getRating()) + " / 5");
    }

    @Override
    public int getItemCount() {
        return nearbyRestaurants.size();
    }

    public static class RestaurantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View imageOverlay;
        TextView nameOfRestaurant, ratingOfRestaurant;
        ImageView photoOfRestaurant;
        OnRestaurantListener onRestaurantListener;
//        RatingBar ratingOfRestaurant;

        public RestaurantsViewHolder(@NonNull View itemView, OnRestaurantListener onRestaurantListener) {
            super(itemView);

            //Hooks
            nameOfRestaurant = itemView.findViewById(R.id.placeNameAlt);
            photoOfRestaurant = itemView.findViewById(R.id.placePhotoAlt);
            ratingOfRestaurant = itemView.findViewById(R.id.placeRatingAlt);
            imageOverlay = itemView.findViewById(R.id.imageOverlayRes);

            this.onRestaurantListener = onRestaurantListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRestaurantListener.onRestaurantClick(getAdapterPosition(), nameOfRestaurant, photoOfRestaurant, ratingOfRestaurant, imageOverlay);
        }
    }

    public interface OnRestaurantListener{
        void onRestaurantClick(int position, TextView placeName, ImageView placePhoto, TextView placeRating, View imageOverlay);
    }
}
