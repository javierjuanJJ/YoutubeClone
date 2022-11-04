package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Models.PlaylistModel;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.R;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PlaylistModel> list;
    private OnItemClick listener;

    public PlayListAdapter(Context context, ArrayList<PlaylistModel> list, OnItemClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.playlist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.blind(list.get(position), listener);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView txt_playlist_name, txt_videos_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt_playlist_name = itemView.findViewById(R.id.txt_plane_name);
            txt_videos_count = itemView.findViewById(R.id.txt_videos_count);
        }

        public void blind(final PlaylistModel playlistModel, final OnItemClick listener){
            txt_playlist_name.setText(playlistModel.getPlaylist_name());
            txt_videos_count.setText(String.format(context.getString(R.string.txtVideosCount), playlistModel.getVideos()));
            txt_playlist_name.setOnClickListener(view -> listener.onItemClick(playlistModel,listener));
            txt_videos_count.setOnClickListener(view -> listener.onItemClick(playlistModel,listener));
        }


    }

    public interface OnItemClick {
        void onItemClick (PlaylistModel model, final OnItemClick listener);
    }
}
