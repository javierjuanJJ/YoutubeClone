package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Constants.FieldsConstants;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Models.ContentModel;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.R;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ContentModel> list;

    public ContentAdapter(Context context, ArrayList<ContentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video, parent, false));
    }

    DatabaseReference reference;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentModel model = list.get(position);
        if (model != null) {
            Glide.with(context).asBitmap().load(model.getVideoUrl()).into(holder.thumbnail);
            holder.videoTitle.setText(context.getString(R.string.videoTitle, model.getViews()));
            holder.date.setText(model.getDate());
            setData(model.getPublisher(), holder.channelLogo, holder.channelName);
        }
    }

    private void setData(String publisher, CircleImageView logo, TextView channelName) {
        reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.CHANNELS_FIELD);
        reference.child(publisher).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String cName = snapshot.child(FieldsConstants.CHANNEL_NAME_FIELD).getValue().toString();
                    String cLogo = snapshot.child(FieldsConstants.CHANNEL_LOGO_FIELD).getValue().toString();
                    channelName.setText(cName);
                    Picasso.get().load(cLogo).placeholder(R.drawable.ic_launcher_background).into(logo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView videoTitle, channelName, views, date;
        private CircleImageView channelLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            videoTitle = itemView.findViewById(R.id.video_title);
            channelName = itemView.findViewById(R.id.channel_name);
            views = itemView.findViewById(R.id.views_count);
            date = itemView.findViewById(R.id.date);
            channelLogo = itemView.findViewById(R.id.channel_logo);
        }
    }
}
