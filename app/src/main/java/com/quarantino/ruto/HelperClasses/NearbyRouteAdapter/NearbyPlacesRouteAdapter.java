package com.quarantino.ruto.HelperClasses.NearbyRouteAdapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class NearbyPlacesRouteAdapter extends RecyclerView.Adapter<NearbyPlacesRouteAdapter.NearbyPlaceViewHolder>{

    ArrayList<NearbyPlacesRouteHelperClass> nearbyTypePlaceList;
    Resources context;

    public NearbyPlacesRouteAdapter(ArrayList<NearbyPlacesRouteHelperClass> nearbyTypePlaceList) {
        this.nearbyTypePlaceList = nearbyTypePlaceList;
    }

    @NonNull
    @Override
    public NearbyPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_place_route_card, parent, false);
        NearbyPlaceViewHolder nearbyPlaceViewHolder = new NearbyPlaceViewHolder(view);

        context = parent.getContext().getResources();
        return nearbyPlaceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlaceViewHolder holder, int position) {
        NearbyPlacesRouteHelperClass nearbyPlacesRouteHelperClass = nearbyTypePlaceList.get(position);
        holder.placeName.setText(nearbyPlacesRouteHelperClass.getPlaceName());
        holder.placePhoto.setImageBitmap(nearbyPlacesRouteHelperClass.getPlacePhoto());
    }

    @Override
    public int getItemCount() {
        return nearbyTypePlaceList.size();
    }

    public static class NearbyPlaceViewHolder extends RecyclerView.ViewHolder{

        TextView placeName;
        ImageView placePhoto;

        public NearbyPlaceViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            placeName = itemView.findViewById(R.id.nearbyPlaceName);
            placePhoto = itemView.findViewById(R.id.nearbyPlacePhoto);
        }
    }
}
