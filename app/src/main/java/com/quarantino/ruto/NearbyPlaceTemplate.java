package com.quarantino.ruto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quarantino.ruto.HelperClasses.JsonParserPlace;
import com.quarantino.ruto.HelperClasses.ReviewAdapter.ReviewAdapter;
import com.quarantino.ruto.HelperClasses.ReviewAdapter.ReviewHelperClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearbyPlaceTemplate extends AppCompatActivity {

    private RecyclerView reviewRecycler;
    private RecyclerView.Adapter reviewRecyclerAdapter;
    private ArrayList<ReviewHelperClass> reviewOfPlace = new ArrayList<>();

    private TextView nameOfPlace, openStatus;
    private RatingBar ratingOfPlace;
    private ImageView photoOfPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_place_template);

        openStatus = findViewById(R.id.openingHours);
        nameOfPlace = findViewById(R.id.placeName);
        ratingOfPlace = findViewById(R.id.placeRating);
        photoOfPlace = findViewById(R.id.placeImage);


        reviewRecycler = findViewById(R.id.reviewRecycler);
        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


        //Data from last activity
        String nameOfPlaceStr = getIntent().getStringExtra("Name of Place");
        float ratingOfPlaceVal = getIntent().getFloatExtra("Rating of Place", 0);
        byte[] byteArray = getIntent().getExtras().getByteArray("Image");
        Bitmap imageOfPlace = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        String idOfPlace = getIntent().getStringExtra("Id Of Place");

        ratingOfPlace.setRating(ratingOfPlaceVal);
        nameOfPlace.setText(nameOfPlaceStr);
        photoOfPlace.setImageBitmap(imageOfPlace);

        Log.d("Id Of Place", idOfPlace);
        getPlaceDetails(idOfPlace);
    }

    private void getPlaceDetails(String placeId) {

        // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ-yw3J0bkDDkRMTxIHAK_Ous&key=AIzaSyA5L81_-5d2Hy7hHsNVhodk1zS90Qu-aP8

        String url = "https://maps.googleapis.com/maps/api/place/details/json" +
                "?placeid=" + placeId +
                "&key=" + getResources().getString(R.string.places_api_key);

        Log.d("Json URL", url);

        new PlaceTask().execute(url);
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new placeDetailsTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();

        Log.d("Process", "Url downloaded");
        Log.d("Data downloaded", data);

        return data;
    }

    private class placeDetailsTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParserPlace jsonParser = new JsonParserPlace();

            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
                Log.d("JSON Parsed", "Positive");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            HashMap<String, String> hashMapList = hashMaps.get(0);

//            String name = hashMapList.get("name");
//            nameOfPlace.setText(name);
            String open_now = hashMapList.get("open_now");
            if(open_now != null){
                if (open_now == "true")
                    openStatus.setText("Open Now");
                else
                    openStatus.setText("Closed Now");
            }
            String author = hashMapList.get("review_author");
            String review = hashMapList.get("review_text");

            reviewOfPlace.add(new ReviewHelperClass(author, review));
            reviewRecycler();
//            Log.d("Author", author);
        }
    }

    private void reviewRecycler(){
        reviewOfPlace.add(new ReviewHelperClass("Eric", getResources().getString(R.string.obText2)));
        reviewRecyclerAdapter = new ReviewAdapter(reviewOfPlace);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
