package com.example.csm117.chefinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by albertli on 11/29/17.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private String[] names;
    private String[] pictures;
    private String[] recipes;
    private static LayoutInflater inflater=null;
    public ImageAdapter(RecipesFragment recipesFragment, String[] name, String[] pic, String[] recipe) {
        // TODO Auto-generated constructor stub
        names=name;
        mContext=recipesFragment.getActivity();
        pictures=pic;
        recipes=recipe;
        inflater = ( LayoutInflater ) recipesFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return names.length;
    }

    public Object getItem(int position) {
        return names[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        TextView textView;
        View rowView;

   //     if (convertView == null) {

            rowView = inflater.inflate(R.layout.grid_layout, null);
            imageView = (ImageView) rowView.findViewById(R.id.images);
            textView = (TextView) rowView.findViewById(R.id.texts);
            textView.setText(names[position]);

            //System.out.println("position is " + position);
            //System.out.println("name is " + names[position]);


            String url = pictures[position];
            //System.out.println("picture is " + url);
            //System.out.println("url is " + recipes[position]);


            Picasso.with(this.mContext).load(url)
                    .resize(200, 200).into(imageView);
//        }
//        else    {
//            rowView = (View)convertView;
//        }


        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(mContext);

                ad.setCancelable(true);
                ad.setIcon(R.drawable.chefinder_logo);
                ad.setTitle("Instructions");
                final String site = recipes[position];
                ad.setMessage(site);
                ad.setPositiveButton("Continue to website", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", site);
                        mContext.startActivity(intent);

                    }
                });
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                });

                AlertDialog alert = ad.create();
                alert.show();
            }
        });


        return rowView;

    }
}