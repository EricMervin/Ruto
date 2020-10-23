package com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter;

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

public class SelectedPlacesAdapter extends RecyclerView.Adapter<SelectedPlacesAdapter.CategoriesViewHolder>{

    ArrayList<SelectedPlacesHelperClass> selectedPlacesList;
    Resources context;

    public SelectedPlacesAdapter(ArrayList<SelectedPlacesHelperClass> selectedPlacesList) {
        this.selectedPlacesList = selectedPlacesList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_place_card, parent, false);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);

        context = parent.getContext().getResources();
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        SelectedPlacesHelperClass selectedPlacesHelperClass = selectedPlacesList.get(position);
        holder.nameOfPlace.setText(selectedPlacesHelperClass.getNameOfSelectedPlace());
        holder.photoOfPlace.setImageBitmap(selectedPlacesHelperClass.getPhotoOfSelectedPlace());
    }

    @Override
    public int getItemCount() {
        return selectedPlacesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfPlace;
        ImageView photoOfPlace;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            nameOfPlace = itemView.findViewById(R.id.selectedPlaceName);
            photoOfPlace = itemView.findViewById(R.id.selectedPlaceImage);
        }
    }
}
