package raigar.ramnarayan.cloudnaryimageupload;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor cursor;
    private int imageWidth, imageHeight;
    private  ArrayList<ArrayList<String>> arr;

    // Constructor
    public ImageAdapter(Context c, Cursor cursor, int imageWidth, int imageHeight) {
        mContext = c;
        this.cursor = cursor;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        getValues();
    }

    public int getCount() {
        Log.v("asfdasdfsadfsad" ,  arr.size() + "size");
        return arr.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
       /* ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
      //  imageView.setImageResource(mThumbIds[position]);

        Picasso.with(mContext).load(arr.get(position).get(1)).
                resize(imageWidth, imageHeight).into(imageView);
        return imageView;*/

        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

         //   grid = new View(mContext);
            grid = inflater.inflate(R.layout.item_image, null);
            ImageView imageView = grid.findViewById(R.id.image);

            imageView.requestLayout();
            imageView.getLayoutParams().height = imageWidth;
            imageView.getLayoutParams().width = imageHeight;

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(mContext).load(arr.get(position).get(0))
                    .into(imageView);

        /*    Glide
                    .with(mContext)
                    .load(arr.get(position).get(0))
                    .placeholder(R.drawable.ic_plus)
                    .into(imageView);*/


            Log.v("asfdasdfsadfsad", arr.get(position).get(0));

        } else {
            grid =  convertView;
        }

        return grid;

    }

    private void getValues() {
        if (arr == null)
        arr = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{

                ArrayList<String> list = new ArrayList<>();

                list.add(cursor.getString(0));
                list.add(cursor.getString(1));

                arr.add(list);
            } while(cursor.moveToNext());
        }

      //  Log.v("asdfasdf", arr.size() + "size");
      //  return arr;
    }
}
