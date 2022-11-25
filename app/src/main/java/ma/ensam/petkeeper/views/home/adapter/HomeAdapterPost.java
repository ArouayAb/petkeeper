package ma.ensam.petkeeper.views.home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.ensam.petkeeper.config.app.AppConfig;
import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.models.HomeOffers;

public class HomeAdapterPost extends RecyclerView.Adapter<HomeAdapterPost.ViewModel>{

    List<HomeOffers> homeOffers;
    private HomeAdapterPost.ItemClickedListener mItemListener;

    public HomeAdapterPost(ArrayList<HomeOffers> offerWithProfiles, HomeAdapterPost.ItemClickedListener mItemListener) {
        //this.petCategories = petCategories;
        this.homeOffers =  offerWithProfiles;
        this.mItemListener = mItemListener;
    }
    @NonNull
    @Override
    public HomeAdapterPost.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home_post,parent,false);
        return new HomeAdapterPost.ViewModel(inflate);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull HomeAdapterPost.ViewModel holder, int position) {
        String duration = String.valueOf(((homeOffers.get(position).getTo().getTime()- homeOffers.get(position).getFrom().getTime())/(60*60*24)));
        holder.postUserName.setText(homeOffers.get(position).getUserName());
        holder.postPet.setText(homeOffers.get(position).getPet().name());
        holder.postFrom.setText(AppConfig.dateFormat.format(homeOffers.get(position).getFrom()));
        holder.postTo.setText(AppConfig.dateFormat.format(homeOffers.get(position).getTo()));
        holder.postDuration.setText(duration);
        holder.postDesc.setText(homeOffers.get(position).getDescription());

        holder.constraintLayout.setOnClickListener(view -> {
            mItemListener.onClickItem(homeOffers.get(position));
            notifyDataSetChanged();
        });
        holder.seePostLayout.setOnClickListener(view -> {
            mItemListener.onCLickSeePost(homeOffers.get(position));
        });
        holder.postImage.setOnClickListener(view -> {
            mItemListener.onClickSeeProfile(homeOffers.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return homeOffers.size();
    }

    public interface ItemClickedListener {
        void onClickItem(HomeOffers postHome);
        void onCLickSeePost(HomeOffers postHome);
        void onClickSeeProfile(HomeOffers postHome);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateRecyclerView(List<HomeOffers> offers){
        this.homeOffers = offers ;
        notifyDataSetChanged();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        TextView postUserName, postPet, postFrom, postTo, postDuration,postDesc;
        ImageView postImage;
        ConstraintLayout constraintLayout;
        LinearLayout seePostLayout;

        public ViewModel(@NonNull View itemView) {
            super(itemView);
            postUserName = itemView.findViewById(R.id.user_name_id);
            postDesc = itemView.findViewById(R.id.post_desc_id);
            postPet = itemView.findViewById(R.id.pet_id);
            postFrom = itemView.findViewById(R.id.from_id);
            postTo = itemView.findViewById(R.id.to_id);
            postDuration = itemView.findViewById(R.id.duration_id);
            postImage = itemView.findViewById(R.id.post_profile_image_id);
            constraintLayout = itemView.findViewById(R.id.post_constraint_id);
            seePostLayout = itemView.findViewById(R.id.see_post_id);

        }
    }
}
