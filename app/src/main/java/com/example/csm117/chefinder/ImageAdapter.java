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
        inflater = ( LayoutInflater ) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView text;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        TextView textView;
        View rowView;

        if (convertView == null) {

            rowView = inflater.inflate(R.layout.grid_layout, null);
            imageView = (ImageView) rowView.findViewById(R.id.images);
            textView = (TextView) rowView.findViewById(R.id.texts);
            textView.setText("boosted");
            //textView.setText(result[position]);

            String url = "http://static.food2fork.com/266803a7e1.jpg";
            if (position == 2)
                url = "http://static.food2fork.com/tamalecornbreaddressing_DSC14415f3b3fc8.jpg";
            if (position == 3)
                url = "http://static.food2fork.com/IMG_1059180x180a74c.jpg";

            //Picasso.with(this.mContext).load(imageIds[position])
            //        .resize(200, 200).into(imageView);

            Picasso.with(this.mContext).load(url)
                    .resize(200, 200).into(imageView);
        }
        else    {
            rowView = (View)convertView;
        }

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(mContext);

                ad.setCancelable(true);
                ad.setIcon(R.drawable.chefinder_logo);
                ad.setTitle("Instructions");
                final String site = "http://allrecipes.com/recipe/40057/autumn-spice-ham-steak/";
                //final String site = recipes[position];
                ad.setMessage(site);
                ad.setPositiveButton("Continue to website", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", site);
                        mContext.startActivity(intent);

                    }
                });
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        // Do something else
                    }
                });

                AlertDialog alert = ad.create();
                alert.show();
            }
        });

        return rowView;

    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5
    };
}