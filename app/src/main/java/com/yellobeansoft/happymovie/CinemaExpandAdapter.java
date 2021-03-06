package com.yellobeansoft.happymovie;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by Beboyz on 1/22/15 AD.
 */
public class CinemaExpandAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CinemaGroup> cinemaGroupList;
    private ArrayList<CinemaGroup> cinemaGroupOriList;
    private Cinema objCinema;
    private CinemaFavorite objCinemaFav;
    private Context mContext;

    public CinemaExpandAdapter(Context context, ArrayList<CinemaGroup> cinemaGroupList) {
        this.context = context;
        mContext = context;
        this.cinemaGroupList = new ArrayList<CinemaGroup>();
        this.cinemaGroupList.addAll(cinemaGroupList);
        this.cinemaGroupOriList = new ArrayList<CinemaGroup>();
        this.cinemaGroupOriList.addAll(cinemaGroupList);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Cinema> cinemaList = cinemaGroupList.get(groupPosition).getCinema();
        return cinemaList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {



        Cinema cinema = (Cinema) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_cinema_allitem, null);
        }

        TextView name = (TextView) view.findViewById(R.id.txtCinema);
        TextView nameTH = (TextView) view.findViewById(R.id.txtCinemaTH);
        Button favImg = (Button) view.findViewById(R.id.btnFavourite);
        TextView distance = (TextView) view.findViewById(R.id.txtDistance);
        name.setText(cinema.getName().trim());
        nameTH.setText(cinema.getNameTH());



        objCinemaFav = new CinemaFavorite();
        objCinema = cinemaGroupList.get(groupPosition).getCinema().get(childPosition);
        double dist = objCinema.getDistance();
        String txtDistance = String.format("%.2f", dist);
        if (dist > 0){
            distance.setText(txtDistance+"km");
        }

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (objCinemaFav.checkExist(context, objCinema)){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                favImg.setBackgroundDrawable(favImg.getContext().getResources().getDrawable(R.drawable.ic_favon));
            } else {
                favImg.setBackground(favImg.getContext().getResources().getDrawable(R.drawable.ic_favon));
            }
        } else {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                favImg.setBackgroundDrawable(favImg.getContext().getResources().getDrawable(R.drawable.ic_favoff));
            } else {
                favImg.setBackground(favImg.getContext().getResources().getDrawable(R.drawable.ic_favoff));
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objCinema = cinemaGroupList.get(groupPosition).getCinema().get(childPosition);
//                Toast.makeText(context, objCinema.getName(),
//                        Toast.LENGTH_SHORT).show();

                new LoadingDialog().execute();

                Intent intent = new Intent(v.getContext(), ShowtimeCinemaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("chooseCinema",objCinema);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);

            }
        });

        favImg.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                objCinema = cinemaGroupList.get(groupPosition).getCinema().get(childPosition);
                if (objCinemaFav.checkExist(context, objCinema)) {
                    objCinemaFav.removeFavorite(context, objCinema);
                } else {
                    objCinemaFav.addFavorite(context, objCinema);
                }

                notifyDataSetChanged();
            }
        });


        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<Cinema> CinemaList = cinemaGroupList.get(groupPosition).getCinema();
        return CinemaList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return cinemaGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return cinemaGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        CinemaGroup CinemaGroup = (CinemaGroup) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_cinema_allgroup, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.txtCinemaGrp);
        heading.setText(CinemaGroup.getCinemaGroup().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {


        return true;
    }

    public void filterData(String query){

        query = query.toLowerCase();
        cinemaGroupList.clear();

        if(query.isEmpty()){
            cinemaGroupList.addAll(cinemaGroupOriList);
        }
        else {

            for(CinemaGroup CinemaGroup: cinemaGroupOriList){

                ArrayList<Cinema> CinemaList = CinemaGroup.getCinema();
                ArrayList<Cinema> newList = new ArrayList<Cinema>();
                for(Cinema Cinema: CinemaList){
                    if(Cinema.getName().toLowerCase().contains(query) ||
                            Cinema.getNameTH().toLowerCase().contains(query)){
                        newList.add(Cinema);
                    }
                }
                if(newList.size() > 0){
                    CinemaGroup nCinemaGroup = new CinemaGroup(CinemaGroup.getCinemaGroup(),newList);
                    cinemaGroupList.add(nCinemaGroup);
                }
            }
        }
        notifyDataSetChanged();

    }

    class LoadingDialog extends AsyncTask<String, Integer, String> {

        private ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            mProgress = new ProgressDialog(mContext, R.style.Happy_Dialog_Style);
            mProgress.setMessage("Loading...");
            mProgress.setCancelable(true);
            mProgress.setIndeterminate(true);
            mProgress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("CINEMA-TEST", "Start Sync");
                DataLoader objLoader = new DataLoader(mContext);
                objLoader.syncAll();
                Log.d("CINEMA-TEST", "Execute Sync");
                sleep(500);
                while (!objLoader.checkShowTimeSyncDone() || !objLoader.checkMovieSyncDone() || !objLoader.checkMovieSyncDone()) {
                }
                Log.d("CINEMA-TEST", "Finish Sync");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);
        }

        @Override
        protected void onPostExecute(String testStr) {
            mProgress.dismiss();

        }

    }

}