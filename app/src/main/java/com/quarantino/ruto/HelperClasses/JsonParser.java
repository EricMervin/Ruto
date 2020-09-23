package com.quarantino.ruto.HelperClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String,String> parseJsonObject(JSONObject object){

        HashMap<String,String> dataList = new HashMap<>();
        try {
            String name = object.getString("name");
            String rating = object.getString("rating");

//            String photoRef = object.getJSONObject("photo").getString("photo_reference");
//            String openNow = object.getJSONObject("opening_hours").getString("open_now");

            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");

            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");

            dataList.put("name", name);
            dataList.put("rating", rating);
//            dataList.put("photo_reference", photoRef);
//            dataList.put("open_now", openNow);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private List<HashMap<String,String>> parseJSONArray(JSONArray jsonArray){

        List<HashMap<String,String>> datalist = new ArrayList<>();
        for(int j=0 ; j<jsonArray.length() ; j++){
            try{
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(j));

                datalist.add(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return datalist;
        }

        public List<HashMap<String,String>> parseResult(JSONObject object){

            JSONArray jsonArray = null;

            try {
                jsonArray = object.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            assert null != jsonArray;
            return parseJSONArray(jsonArray);
        }
}
