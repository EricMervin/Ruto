package com.quarantino.ruto.HelperClasses.ReviewAdapter;

public class ReviewHelperClass {

    String reviewText, reviewAuthor;

    public ReviewHelperClass(String reviewAuthor, String reviewText) {
        this.reviewAuthor = reviewAuthor;
        this.reviewText = reviewText;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewText() {
        return reviewText;
    }
}
