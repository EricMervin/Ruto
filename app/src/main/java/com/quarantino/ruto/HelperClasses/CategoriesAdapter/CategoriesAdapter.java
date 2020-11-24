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

    private ArrayList<CategoriesHelperClass> categoriesList;
    private OnCategoryTypeListener onCategoryTypeListener;
    Resources context;

    public CategoriesAdapter(ArrayList<CategoriesHelperClass> categoriesList, OnCategoryTypeListener onCategoryTypeListener) {
        this.categoriesList = categoriesList;
        this.onCategoryTypeListener = onCategoryTypeListener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_card, parent, false);

        context = parent.getContext().getResources();

        return new CategoriesViewHolder(view, onCategoryTypeListener);
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

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryType;
        ImageView categoryIcon;
        OnCategoryTypeListener onCategoryTypeListener;

        public CategoriesViewHolder(@NonNull View itemView, OnCategoryTypeListener onCategoryTypeListener) {
            super(itemView);

            //Hooks
            categoryType = itemView.findViewById(R.id.categoryTitle);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            this.onCategoryTypeListener = onCategoryTypeListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCategoryTypeListener.onCategoryTypeClick(getAdapterPosition(), categoryType);
        }
    }

    public interface OnCategoryTypeListener {
        void onCategoryTypeClick(int position, TextView categoryType);
    }
}
