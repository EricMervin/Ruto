package com.quarantino.ruto.HelperClasses.NearbyRouteAdapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.R;

import java.util.ArrayList;

public class NearbyPlacesRouteAdapter extends RecyclerView.Adapter<NearbyPlacesRouteAdapter.NearbyPlacesRouteViewHolder> {

    private ArrayList<NearbyPlacesRouteHelperClass> nearbyTypePlaceList;
    private OnNearbyPlaceRouteListener onNearbyPlaceRouteListener;
    Resources context;

    public NearbyPlacesRouteAdapter(ArrayList<NearbyPlacesRouteHelperClass> nearbyTypePlaceList, OnNearbyPlaceRouteListener onNearbyPlaceRouteListener) {
        this.nearbyTypePlaceList = nearbyTypePlaceList;
        this.onNearbyPlaceRouteListener = onNearbyPlaceRouteListener;
    }

    @NonNull
    @Override
    public NearbyPlacesRouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_place_route_card, parent, false);
//        NearbyPlacesRouteViewHolder nearbyPlacesRouteViewHolder = new NearbyPlacesRouteViewHolder(view);

        context = parent.getContext().getResources();
        return new NearbyPlacesRouteViewHolder(view, onNearbyPlaceRouteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlacesRouteViewHolder holder, int position) {
        NearbyPlacesRouteHelperClass nearbyPlacesRouteHelperClass = nearbyTypePlaceList.get(position);
        holder.placeName.setText(nearbyPlacesRouteHelperClass.getNameOfPlace());
        holder.placePhoto.setImageBitmap(nearbyPlacesRouteHelperClass.getImageOfPlace());
        holder.ratingPlace.setText(String.valueOf(nearbyPlacesRouteHelperClass.getRating()));
        if(nearbyPlacesRouteHelperClass.getOpenNowStatus() == "true"){
            holder.openPlace.setText("Open Now");
            holder.openPlace.setTextColor(context.getColor(R.color.checkGreen));
        } else {
            holder.openPlace.setText("Closed");
            holder.openPlace.setTextColor(context.getColor(R.color.customError));
        }
//        holder.isAdded = nearbyPlacesRouteHelperClass.getPlaceAddedStatus();
//        boolean placeIsAdd = holder.isAdded;
//
//        if(placeIsAdd){
//            holder.addPlaceButton.setBackgroundResource(R.drawable.circle_button_check);
//            holder.iconButton.setImageResource(R.drawable.tick_icon);
//        } else{
//            holder.addPlaceButton.setBackgroundResource(R.drawable.circle_button_uncheck);
//            holder.iconButton.setImageResource(R.drawable.plus_icon);
//        }
//
//        nearbyPlacesRouteHelperClass.setPlaceAddedStatus(!holder.isAdded);
//        holder.isAdded = nearbyPlacesRouteHelperClass.getPlaceAddedStatus();
    }

    @Override
    public int getItemCount() {
        return nearbyTypePlaceList.size();
    }

    public static class NearbyPlacesRouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView placeName, ratingPlace, openPlace;
        ImageView placePhoto, iconButton;
        FrameLayout addPlaceButton;
        boolean isAdded = false;
        OnNearbyPlaceRouteListener onNearbyPlaceRouteListener;

        public NearbyPlacesRouteViewHolder(@NonNull View itemView, final OnNearbyPlaceRouteListener onNearbyPlaceRouteListener) {
            super(itemView);

            //Hooks
            addPlaceButton = itemView.findViewById(R.id.addButton);
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
                        addPlaceButton.setBackgroundResource(R.drawable.circle_button_check);
                        iconButton.setImageResource(R.drawable.tick_icon);
                    } else{
                        addPlaceButton.setBackgroundResource(R.drawable.circle_button_uncheck);
                        iconButton.setImageResource(R.drawable.plus_icon);
                    }
                    onNearbyPlaceRouteListener.onAddPlaceRouteClick(getAdapterPosition(), isAdded);
                    isAdded = !isAdded;
                }
            });
        }

        @Override
        public void onClick(View view) {
            onNearbyPlaceRouteListener.onNearbyPlaceRouteClick(getAdapterPosition(), placeName, placePhoto);
        }
    }

    public interface OnNearbyPlaceRouteListener{
        void onNearbyPlaceRouteClick(int position, TextView placeName, ImageView placePhoto);
        void onAddPlaceRouteClick(int position, boolean isAdded);
    }


}
