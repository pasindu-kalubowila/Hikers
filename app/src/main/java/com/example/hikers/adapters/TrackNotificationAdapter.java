package com.example.hikers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hikers.R;
import java.util.List;

public class TrackNotificationAdapter extends RecyclerView.Adapter<TrackNotificationAdapter.ImageViewHolder> {

    private Context mContext;
    private List<TrackNotificationAdapter> mtrack_notificationAdapters;

    public TrackNotificationAdapter(Context context, List<TrackNotificationAdapter> track_notificationAdapters) {
        mContext = context;
        mtrack_notificationAdapters = track_notificationAdapters;
    }

    @NonNull
    @Override
    public TrackNotificationAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item_track, parent, false);
        return new TrackNotificationAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrackNotificationAdapter.ImageViewHolder holder, final int position) {


    }


    @Override
    public int getItemCount() {
        return mtrack_notificationAdapters.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile;
        public ImageView btn_yes;
        public ImageView btn_no;
        public TextView username, full_username, text;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile_track);
            btn_yes = itemView.findViewById(R.id.btn_yes_track);
            btn_no = itemView.findViewById(R.id.btn_no_track);
            username = itemView.findViewById(R.id.username_track);
            full_username = itemView.findViewById(R.id.fullname_track);

        }
    }

}
