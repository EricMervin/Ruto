package com.quarantino.ruto.HelperClasses.ReviewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    ArrayList<ReviewHelperClass> reviewList;

    public ReviewAdapter(ArrayList<ReviewHelperClass> reviewList){
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewHelperClass reviewHelperClass = reviewList.get(position);
        holder.reviewAuth.setText(reviewHelperClass.getReviewAuthor());
        holder.reviewTxt.setText(reviewHelperClass.getReviewText());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView reviewAuth, reviewTxt;

        public ReviewViewHolder(@NonNull View itemView){
            super(itemView);

            reviewAuth = itemView.findViewById(R.id.reviewAuthor);
            reviewTxt = itemView.findViewById(R.id.reviewText);
        }
    }
}
