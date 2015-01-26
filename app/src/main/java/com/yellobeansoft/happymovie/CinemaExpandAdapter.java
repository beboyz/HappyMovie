package com.yellobeansoft.happymovie;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Beboyz on 1/22/15 AD.
 */
public class CinemaExpandAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CinemaGroup> cinemaGroupList;
    private ArrayList<CinemaGroup> cinemaGroupOriList;
    private Cinema objCinema;
    private ContextProvider objContext;
    private CinemaFavorite objCinemaFav;

    public CinemaExpandAdapter(Context context, ArrayList<CinemaGroup> cinemaGroupList) {
        this.context = context;
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
        name.setText(cinema.getName().trim());
        nameTH.setText("เมเจอร์ ซินีเพล็กซ์");

        objContext = new ContextProvider();
        objCinemaFav = new CinemaFavorite();
        objCinema = cinemaGroupList.get(groupPosition).getCinema().get(childPosition);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (objCinemaFav.checkExist(objContext.getContext(), objCinema)){
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

        favImg.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                objCinema = cinemaGroupList.get(groupPosition).getCinema().get(childPosition);
                if (objCinemaFav.checkExist(objContext.getContext(), objCinema)) {
                    objCinemaFav.removeFavorite(objContext.getContext(), objCinema);
                } else {
                    objCinemaFav.addFavorite(objContext.getContext(), objCinema);
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
                            Cinema.getName().toLowerCase().contains(query)){
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

}