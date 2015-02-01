package com.yellobeansoft.happymovie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;

/**
 * Created by Beboyz on 1/18/15 AD.
 */

public class MovieStaggeredActivity extends Activity {

    private StaggeredGridView mGridView;
    private MovieStaggeredAdapter mAdapter;
//    private ArrayList<String> mDataset;

    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_staggered);


        ArrayList<String> mDataset = new ArrayList<String>();
        MovieTable objMovieTab = new MovieTable(MovieStaggeredActivity.this);
        ArrayList<Movies> movieList = new ArrayList<Movies>();
        movieList = objMovieTab.getAllMovies();

        if (movieList.size() > 0) {
            for (int i = 0; i < movieList.size(); i++) {
                Movies objMovie = (Movies) movieList.get(i);
                String img = objMovie.getMovieImg();
                mDataset.add(img);
            }
            mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
            mAdapter = new MovieStaggeredAdapter(MovieStaggeredActivity.this, R.id.image);
            //mAdapter = new MovieStaggeredAdapter(MovieStaggeredActivity.this, R.id.image, movieList);
            for (String data : mDataset) {
                mAdapter.add(data);
            }
            mGridView.setAdapter(mAdapter);
        }



//        new LoadMovieAsync().execute();

    }





    class LoadMovieAsync extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MovieStaggeredActivity.this);
            pDialog.setMessage("Loading Movie ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected ArrayList<String> doInBackground(String... params) {

            ArrayList<String> mDataset = new ArrayList<String>();
            MovieTable objMovieTab = new MovieTable(MovieStaggeredActivity.this);
            ArrayList<Movies> movieList = new ArrayList<Movies>();

            while ( movieList.size() == 0 ) {
                movieList = objMovieTab.getAllMovies();
            }

            if (movieList.size() == 0){
                Log.d("doInBackground","movieList is null");
            } else {
                Log.d("doInBackground","movieList is NOT null");
                Log.d("doInBackground","movieList Size = " + movieList.size() );

                for (int i = 0; i < movieList.size(); i++) {
                    Movies objMovie = (Movies) movieList.get(i);
                    String img = objMovie.getMovieImg();
                    mDataset.add(img);
                }
            }

            Log.d("doInBackground","End");

            return mDataset;
        }

        @Override
        protected void onProgressUpdate(Integer...integers){}

        /*@Override
        protected void onPostExecute(final ArrayList<Movies> mDataset) {
            MovieTable objMovieTab = new MovieTable(MovieStaggeredActivity.this);
            //final ArrayList<String> innerSet = mDataset;
            final ArrayList<Movies> movies = mDataset;
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {

                public void run() {

                    mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
                    mAdapter = new MovieStaggeredAdapter(MovieStaggeredActivity.this, R.id.image, movies);

                    if (movies == null) {
                        Log.d("onPostExecute", "DataSet is Null");
                    } else {
                        Log.d("onPostExecute", "DataSet is Not Null");
                        for (Movies data : movies) {
                            mAdapter.add(data);
                        }
                        mGridView.setAdapter(mAdapter);
                    }
                    // dismiss the dialog after getting song information
                    pDialog.dismiss();
                }

            });*/

        @Override
        protected void onPostExecute(final ArrayList<String> mDataset) {
            MovieTable objMovieTab = new MovieTable(MovieStaggeredActivity.this);
            final ArrayList<String> innerSet = mDataset;
            final ArrayList<Movies> movies = objMovieTab.getAllMovies();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {

                public void run() {

                    mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
                    mAdapter = new MovieStaggeredAdapter(MovieStaggeredActivity.this, R.id.image);

                    if (innerSet == null) {
                        Log.d("onPostExecute", "DataSet is Null");
                    } else {
                        Log.d("onPostExecute", "DataSet is Not Null");
                        for (String data : innerSet) {
                            mAdapter.add(data);
                        }
                        mGridView.setAdapter(mAdapter);
                    }
                    // dismiss the dialog after getting song information
                    pDialog.dismiss();
                }

            });

        }


    }

}