package raigar.ramnarayan.cloudnaryimageupload;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageRecycleAdapter extends RecyclerView.Adapter<ImageRecycleAdapter.ImageHolder> {
    private Context mContext;
    private Cursor cursor;
    private int imageWidth, imageHeight;
    private  ArrayList<ArrayList<String>> arr;

    public ImageRecycleAdapter(Context c, Cursor cursor, int imageWidth, int imageHeight) {
        mContext = c;
        this.cursor = cursor;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        getValues();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
      /*  Picasso.with(mContext).load(arr.get(position).get(0)).
                resize(imageWidth, imageHeight).into(holder.imageView);*/
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
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
