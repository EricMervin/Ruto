package com.quarantino.ruto.HelperClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParserPlace {
    private HashMap<String, String> parseJsonObject(JSONObject object) {

        HashMap<String, String> dataList = new HashMap<>();
        try {
            String name = object.getString("name");
            String rating = object.getString("rating");

            JSONArray placeReviews = object.getJSONArray("reviews");
//            HashMap<String, String> reviewList = new HashMap<>();
            for(int i = 0; i < 1; i++){
                String author = ((JSONObject) placeReviews.get(i)).getString("author_name");
                String reviewText = ((JSONObject) placeReviews.get(i)).getString("text");
//                reviewList.put("Review Author", author);
//                reviewList.put("Review Text", author);
                dataList.put("review_author", author);
                dataList.put("review_text", reviewText);
            }

            String contact = object.getString("international_phone_number");

            String placeId = object.getString("place_id");
            String openNow = object.getJSONObject("opening_hours").getString("open_now");

            String latVal = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            String lngVal = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            dataList.put("name", name);
            dataList.put("contat", contact);
//            dataList.put("rating", rating);
            dataList.put("latitude", latVal);
            dataList.put("longitude", lngVal);
            dataList.put("place_id", placeId);
            dataList.put("open_now", openNow);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private List<HashMap<String, String>> parseJSONArray(JSONObject jsonArray) {

        List<HashMap<String, String>> datalist = new ArrayList<>();
            HashMap<String, String> data = parseJsonObject(jsonArray);
            datalist.add(data);
        return datalist;
    }

    public List<HashMap<String, String>> parseResult(JSONObject object) {
        JSONObject jsonObject = null;

        try {
            jsonObject = object.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert null != jsonObject;
        List<HashMap<String, String>> datalist = new ArrayList<>();
        HashMap<String, String> data = parseJsonObject(jsonObject);
        datalist.add(data);

        return datalist;
    }
}