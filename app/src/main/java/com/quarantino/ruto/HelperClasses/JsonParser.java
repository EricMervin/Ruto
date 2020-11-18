 package com.quarantino.ruto.HelperClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    public List<HashMap<String, String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;

        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        assert null != jsonArray;
        return parseJSONArray(jsonArray);
    }

    private List<HashMap<String, String>> parseJSONArray(JSONArray jsonArray) {

        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); j++) {
            try {
                HashMap<String, String> data = parseJsonObject((JSONObject) jsonArray.get(j));
                dataList.add(data);
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }

        return dataList;
    }

    private HashMap<String, String> parseJsonObject(JSONObject object) {

        HashMap<String, String> dataPlace = new HashMap<>();
        try {
            String name = object.getString("name");
            String rating = object.getString("rating");
            String photoRef;

            JSONArray photos = object.getJSONArray("photos");

            for (int i = 0; i < photos.length(); i++) {
                photoRef = ((JSONObject) photos.get(i)).getString("photo_reference");
                dataPlace.put("photo_reference", photoRef);
            }

            String placeId = object.getString("place_id");

            String openNow = object.getJSONObject("opening_hours")
                    .getString("open_now");

            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");

            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            dataPlace.put("name", name);
            dataPlace.put("rating", rating);
            dataPlace.put("lat", latitude);
            dataPlace.put("lng", longitude);
            dataPlace.put("place_id", placeId);
            dataPlace.put("open_now", openNow);
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        return dataPlace;
    }
}