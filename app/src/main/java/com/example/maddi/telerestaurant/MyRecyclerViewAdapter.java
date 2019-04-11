package com.example.maddi.telerestaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maddi on 3/21/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<Map<String, ?>> mDataset;
    private Context mContext;

    // Constructor
    public MyRecyclerViewAdapter(Context myContext, List<Map<String, ?>> myDataset) {
        mContext = myContext;
        mDataset = myDataset;
    }

    // ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView vIcon;
        public TextView vTitle;
        public TextView vDesc;
        public RatingBar vRating;
        public TextView vSite;
        public TextView vPhone;
        public TextView vType;

        public ViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.title);
            vDesc = (TextView) v.findViewById(R.id.desc);
            vRating = (RatingBar) v.findViewById(R.id.rating);
            vSite = (TextView) v.findViewById(R.id.site);
            vPhone = (TextView) v.findViewById(R.id.phone);
            vType = (TextView) v.findViewById(R.id.type);
        }

        public void bindMovieData(Map<String, ?> restaurant) {
            vTitle.setText((String) restaurant.get(("name")));
            vDesc.setText((String) restaurant.get(("address")) + ", " + (String) restaurant.get("region") + ", " + (String) restaurant.get("postcode"));
            vSite.setText((String)restaurant.get("website"));
            vPhone.setText((String) restaurant.get("tel"));
            JSONArray j = null;
            j = (JSONArray)restaurant.get("cuisine");
            String name;
            if(j==null)
                name = "No Information";
            else
            name = j.toString().replace("]","").replace("[","").replace("\""," ");
            vType.setText(name);
            Double bg;
            Float f;
            String a= (String.valueOf(restaurant.get("rating")));
            f = Float.parseFloat(a);
            vRating.setRating(f);
        }
    }

    public static String downloadSingleMovieJson(JSONArray json){
        try {
            JSONArray moviesJsonArray = new JSONArray(json);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                // JSONObject movieJsonObj = new JSONObject(movieJson);
                JSONObject movieJsonObj = (JSONObject) moviesJsonArray.get(i);
                if (movieJsonObj != null) {
                    String name = (String) movieJsonObj.get("cuisine");
                    return name;
                }
            }
        } catch (JSONException ex){
            Log.d("MyDebugMsg", "JSONException in downloadSingleMovieJson");
            ex.printStackTrace();
        }
        return null;
    }

    // Using View Holder
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v;
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Filling Data into ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> restaurant = mDataset.get(position);
        holder.bindMovieData((restaurant));
    }

    // No of items in dataset
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


