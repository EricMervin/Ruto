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

public class NearbyPlacesRouteAdapter extends RecyclerView.Adapter<NearbyPlacesRouteAdapter.CategoriesViewHolder>{

    ArrayList<NearbyPlacesRouteHelperClass> categoriesList;
    Resources context;

    public NearbyPlacesRouteAdapter(ArrayList<NearbyPlacesRouteHelperClass> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_card, parent, false);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);

        context = parent.getContext().getResources();
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        NearbyPlacesRouteHelperClass categoriesHelperClass = categoriesList.get(position);
        holder.categoryType.setText(categoriesHelperClass.getCategoryType());
        holder.categoryIcon.setImageDrawable(categoriesHelperClass.getCategoryIcon());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView categoryType;
        ImageView categoryIcon;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            categoryType = itemView.findViewById(R.id.categoryTitle);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
        }
    }
}
