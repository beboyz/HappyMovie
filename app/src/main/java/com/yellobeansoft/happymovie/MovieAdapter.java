package com.yellobeansoft.happymovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Beboyz on 1/15/15 AD.
 */
public class MovieAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Movies> mMovieList;
    private ViewHolder mViewHolder;

    public MovieAdapter(Context context, ArrayList<Movies> movieLists) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mMovieList = movieLists;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }// method getCount

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }// method getItem

    @Override
    public long getItemId(int position) {
        return position;
    }// method getItemId

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.layout_movie_item, null);

            mViewHolder = new ViewHolder();
            mViewHolder.txtMovieNameEN = (TextView) convertView.findViewById(R.id.txtMovieNameEN);
            mViewHolder.txtMovieNameTH = (TextView) convertView.findViewById(R.id.txtMovieNameTH);
            mViewHolder.txtRating = (TextView) convertView.findViewById(R.id.txtRating);
            mViewHolder.imgIsNew = (ImageView) convertView.findViewById(R.id.imgIsNew);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.txtMovieNameEN.setText(mMovieList.get(position).getMovieTitle());
        mViewHolder.txtMovieNameTH.setText(mMovieList.get(position).getMovieTitleTH());
        mViewHolder.txtRating.setText(mMovieList.get(position).getRating());

        if (mMovieList.get(position).getIsNew()) {
            mViewHolder.imgIsNew.setBackgroundResource(R.drawable.ic_new_released_fast);
        } else {
            mViewHolder.imgIsNew.setBackgroundResource(0);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext = v.getContext();

                Intent intent = new Intent(v.getContext(), ShowtimeMovieActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("chooseMovie", mMovieList.get(position));
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);

            }
        });

        return convertView;
    }// method getView

    private static class ViewHolder {
        public TextView txtMovieNameEN;
        public TextView txtMovieNameTH;
        public TextView txtRating;
        public ImageView imgIsNew;
    }// class ViewHolder




}
