package com.quarantino.ruto.HelperClasses.CategoriesAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>{

    ArrayList<CategoriesHelperClass> categoriesList;
    Resources context;

    public CategoriesAdapter(ArrayList<CategoriesHelperClass> categoriesList) {
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
        CategoriesHelperClass categoriesHelperClass = categoriesList.get(position);
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
