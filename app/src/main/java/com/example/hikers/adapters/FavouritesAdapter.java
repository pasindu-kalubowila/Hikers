package com.example.hikers.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hikers.R;
import com.example.hikers.fragment.EventDataFragment;
import com.example.hikers.fragment.PostDataFragment;
import com.example.hikers.models.Favourite;
import com.example.hikers.models.Post;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private Context context;
    private List<Post> mPosts;
    private List<Favourite> favouriteList;


    public FavouritesAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    public void setFavouriteList(List<Favourite> favouriteList) {
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.photos_item, viewGroup, false);
        return new FavouritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Favourite favourite = favouriteList.get(i);
        Glide.with(context).load(favourite.getImageUrl()).into(viewHolder.post_image);

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString((favourite.isPost() ? "postid" : "eventid"), favourite.getId());
                editor.apply();

                ((FragmentActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, (favourite.isPost() ? new PostDataFragment() : new EventDataFragment()))
                        .addToBackStack("")
                        .commit();
            }
        });

    }


    @Override
    public int getItemCount() {
        return favouriteList == null ? 0 : favouriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image_item);
        }
    }
}
