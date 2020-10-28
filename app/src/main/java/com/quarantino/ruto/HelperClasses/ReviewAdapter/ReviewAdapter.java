package com.quarantino.ruto.HelperClasses.ReviewAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.R;

import java.util.ArrayList;
import java.util.Random;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<ReviewHelperClass> reviewList;
    Resources resourceContext;
    Context context;

    public ReviewAdapter(ArrayList<ReviewHelperClass> reviewList){
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

        resourceContext = parent.getContext().getResources();
        context = parent.getContext();

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewViewHolder holder, int position) {
        final ReviewHelperClass reviewHelperClass = reviewList.get(position);
        holder.reviewAuth.setText(reviewHelperClass.getReviewAuthor());
        holder.reviewTxt.setText(reviewHelperClass.getReviewText());

        //Random photo of review author
        final int randomInt = new Random().nextInt(8);
        switch (randomInt){
            case 0:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_1));
                break;
            case 1:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_2));
                break;
            case 2:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_3));
                break;
            case 3:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_4));
                break;
            case 4:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_5));
                break;
            case 5:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_6));
                break;
            case 6:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_7));
                break;
            case 7:
                holder.authPhoto.setImageDrawable(resourceContext.getDrawable(R.drawable.person_8));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.review_dialog, null);

                de.hdodenhof.circleimageview.CircleImageView authPhoto;
                TextView authName, authReview;

                authPhoto = dialogView.findViewById(R.id.alertAuthPhoto);
                authName = dialogView.findViewById(R.id.alertAuthName);
                authReview = dialogView.findViewById(R.id.alertAuthReview);

//                final TextView authReview = dialogView.findViewById(R.id.alertAuthReview);
//                final Scroller scroller = new Scroller(context);

                authReview.setMovementMethod(new ScrollingMovementMethod());

                authPhoto.setImageDrawable(holder.authPhoto.getDrawable());
                authName.setText(reviewHelperClass.getReviewAuthor());
                authReview.setText(reviewHelperClass.getReviewText());

                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView reviewAuth, reviewTxt;
        ImageView authPhoto;

        public ReviewViewHolder(@NonNull View itemView){
            super(itemView);

            authPhoto = itemView.findViewById(R.id.authPhotoImageView);
            reviewAuth = itemView.findViewById(R.id.reviewAuthor);
            reviewTxt = itemView.findViewById(R.id.reviewText);
        }
    }
}
