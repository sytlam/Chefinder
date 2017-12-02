package com.example.csm117.chefinder;

import android.content.Context;
import android.media.Image;
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
    private String[] result;
    private int[] imageIds;
    private static LayoutInflater inflater=null;
    public ImageAdapter(RecipesFragment recipesFragment, String[] names, int[] images) {
        // TODO Auto-generated constructor stub
        result=names;
        mContext=recipesFragment.getActivity();
        imageIds=images;
        inflater = ( LayoutInflater ) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return imageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView text;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        ImageView imageView;
//
//
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        //for (int i = 0; i < imageIds.length; i++)
//            Picasso.with(this.mContext).load(imageIds[1]).resize(100, 100).into(imageView);
//
//        return imageView;

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.grid_layout, null);
        holder.text =(TextView) rowView.findViewById(R.id.texts);
        holder.img =(ImageView) rowView.findViewById(R.id.images);

        holder.text.setText(result[position]);
        holder.img.setImageResource(imageIds[position]);

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "You Clicked "+result[position], Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;

    }
}