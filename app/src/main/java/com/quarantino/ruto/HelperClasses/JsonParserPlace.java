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

            String cityName = ((JSONObject) object.getJSONArray("address_components").get(2)).getString("long_name");
            String placeAddress = object.getString("formatted_address");

            JSONArray placeReviews = object.getJSONArray("reviews");
            for(int i = 0; i <= 1; i++){
                String author = ((JSONObject) placeReviews.get(i)).getString("author_name");
                String reviewText = ((JSONObject) placeReviews.get(i)).getString("text");
                switch(i){
                    case 0:
                        dataList.put("review_author_1", author);
                        dataList.put("review_text_1", reviewText);
                        break;
                    case 1:
                        dataList.put("review_author_2", author);
                        dataList.put("review_text_2", reviewText);
                }
            }

            String contact = object.getString("international_phone_number");

            String placeId = object.getString("place_id");
            String openNow = object.getJSONObject("opening_hours").getString("open_now");

            String latVal = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            String lngVal = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            dataList.put("name", name);
            dataList.put("city_place", cityName);
            dataList.put("place_address", placeAddress);
            dataList.put("contact", contact);
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