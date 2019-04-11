package com.example.maddi.telerestaurant;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.common.collect.Lists;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

/**
 * Created by maddi on 3/21/2016.
 */
public class RecyclerView_Main extends Fragment {

    private static final String ARG_MOVIE = "R.id.mdf_main_replace";
    String name;
    String address;
    double rating;

    protected Factual factual = new Factual("9klQW2lyBT9nunE0DaUKbwQrnJMx42Qb589Z9izZ", "6ErQltUihEOknBZ1vANd6Qp2BHyA5O3MGuD8zipO");
    protected List<Map<String, ?>> restaurantList = new ArrayList<Map<String, ?>>();

    public static RecyclerView_Main newInstance() {
        RecyclerView_Main fragment = new RecyclerView_Main();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, "R.id.rcmain");
        fragment.setArguments(args);
        return fragment;
    }

    public RecyclerView_Main() {
        // Constructor
    }

    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //RestaurantData restaurantData;
    RecyclerView_Main restaurantData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        //movieData = new MovieData();
        restaurantData = new RecyclerView_Main();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recyclerview_activity, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), restaurantList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerView.setItemAnimator(new ScaleInBottomAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(100);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000);
        mRecyclerView.getItemAnimator().setMoveDuration(100);
        mRecyclerView.getItemAnimator().setChangeDuration(100);
        //mRecyclerView.getItemAnimator().setSupportsChangeAnimations(true);
        ScaleInBottomAnimator animator = new ScaleInBottomAnimator();
        //animator.setSupportsChangeAnimations(true);
        mRecyclerView.setItemAnimator(animator);
        // Adapter Animation
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mRecyclerViewAdapter));
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        alphaAdapter.setDuration(500);
        mRecyclerView.setAdapter(alphaAdapter);


        return rootView;
    }

    // Search action menu
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        // if(menu.findItem((R.id.action_search) == null)
        inflater.inflate(R.menu.menu_actionview, menu);
        super.onCreateOptionsMenu(menu, inflater);



        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if(search!= null){
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    executeSearch(query);
                    //int position = movieData.findFirst(query);
                    //if(position >= 0)
                      //  mRecyclerView.scrollToPosition(position);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    //executeSearch(newText);
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void executeSearch(String city){
        final FactualRetrievalTask task = new FactualRetrievalTask(mRecyclerViewAdapter);
        final Query rquery = new Query()
                .field("locality").equal(city)
                .only("name","address","rating","cuisine","neighborhood","region","postcode","website","tel");

        task.execute(rquery);
    }

    protected class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {
        private final WeakReference<MyRecyclerViewAdapter> adapterReference;

        public FactualRetrievalTask(MyRecyclerViewAdapter adapter){
            adapterReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
        }
        @Override
        protected List<ReadResponse> doInBackground(Query... params) {
            List<ReadResponse> results = Lists.newArrayList();
            for (Query q : params) {
                results.add(factual.fetch("restaurants-us", q));
            }
            //restaurantList = results;
            //String n = results.get("name");

            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(List<ReadResponse> responses) {
            restaurantList.clear();
            for (ReadResponse response : responses)
                for (Map<String, Object> restaurant : response.getData())
                    //restaurantList.add(createRestaurant(results.get(Integer.parseInt("name")),results.get(Integer.parseInt("address"))));
                    restaurantList.add(restaurant);
            StringBuffer sb = new StringBuffer();
            for (ReadResponse response : responses) {
                for (Map<String, Object> restaurant : response.getData()) {

                    String name = (String) restaurant.get("name");
                    //String address = (String) restaurant.get("address");
                    //String type = (String) restaurant.get("type");
                    //Number distance = (Number) restaurant.get("$distance");
                    //sb.append(distance + " meters away: "+name+" @ " +address + ", Type: "+type);
                    sb.append(name);
                    sb.append(System.getProperty("line.separator"));
                }
            }
            //resultText.setText(sb.toString());
            if (adapterReference != null) {
                final MyRecyclerViewAdapter adapter = adapterReference.get();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }

    public  List<Map<String, ?>> getRestaurantsList() {
        return restaurantList;
    }

    public int getSize(){
        return restaurantList.size();
    }
}
