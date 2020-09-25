package com.quarantino.ruto.HelperClasses.RestaurantsAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>{

    ArrayList<RestaurantsHelperClass> nearbyRestaurants;

    public RestaurantsAdapter(ArrayList<RestaurantsHelperClass> nearbyRestaurants) {
        this.nearbyRestaurants = nearbyRestaurants;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_restaurants_card, parent, false);
        RestaurantsViewHolder restaurantsViewHolder = new RestaurantsViewHolder(view);

        return restaurantsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        RestaurantsHelperClass restaurantsHelperClass = nearbyRestaurants.get(position);

        holder.nameOfRestaurant.setText(restaurantsHelperClass.getNameOfPlace());
        holder.photoOfRestaurant.setImageBitmap(restaurantsHelperClass.getImageOfPlace());
        holder.ratingOfRestaurant.setRating(restaurantsHelperClass.rating);
    }

    @Override
    public int getItemCount() {
        return nearbyRestaurants.size();
    }

    public static class RestaurantsViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfRestaurant;
        ImageView photoOfRestaurant;
        RatingBar ratingOfRestaurant;

        public RestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            nameOfRestaurant = itemView.findViewById(R.id.topRestaurantName);
            photoOfRestaurant = itemView.findViewById(R.id.topRestaurantPhoto);
            ratingOfRestaurant = itemView.findViewById(R.id.topRestaurantRating);
        }
    }
}
