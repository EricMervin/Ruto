package com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;

public class SelectedPlacesAdapter extends RecyclerView.Adapter<SelectedPlacesAdapter.SelectedPlacesViewHolder>{

    private ArrayList<SelectedPlacesHelperClass> selectedPlacesList;
    Context context;
    private  OnSelectedPlaceListener onSelectedPlaceListener;

    public SelectedPlacesAdapter(ArrayList<SelectedPlacesHelperClass> selectedPlacesList, OnSelectedPlaceListener onSelectedPlaceListener) {
        this.selectedPlacesList = selectedPlacesList;
        this.onSelectedPlaceListener = onSelectedPlaceListener;
    }

    @NonNull
    @Override
    public SelectedPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_place_card, parent, false);

        context = parent.getContext();
        return new SelectedPlacesViewHolder(view, onSelectedPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPlacesViewHolder holder, int position) {
        SelectedPlacesHelperClass selectedPlacesHelperClass = selectedPlacesList.get(position);
        holder.nameOfPlace.setText(selectedPlacesHelperClass.getNameOfSelectedPlace());
        holder.photoOfPlace.setImageBitmap(selectedPlacesHelperClass.getPhotoOfSelectedPlace());
    }

    @Override
    public int getItemCount() {
        return selectedPlacesList.size();
    }

    public static class SelectedPlacesViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfPlace;
        ImageView photoOfPlace;
        FrameLayout removePlaceButton;
        OnSelectedPlaceListener onSelectedPlaceListener;
//        CardView placeCard;

        public SelectedPlacesViewHolder(@NonNull View itemView, final OnSelectedPlaceListener onSelectedPlaceListener) {
            super(itemView);

            //Hooks
            nameOfPlace = itemView.findViewById(R.id.selectedPlaceName);
            photoOfPlace = itemView.findViewById(R.id.selectedPlaceImage);
            removePlaceButton = itemView.findViewById(R.id.removeButton);
            this.onSelectedPlaceListener = onSelectedPlaceListener;

            removePlaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectedPlaceListener.onRemovePlaceClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnSelectedPlaceListener{
        void onRemovePlaceClick(int position);
    }
}
