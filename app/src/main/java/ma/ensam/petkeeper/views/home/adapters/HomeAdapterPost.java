package ma.ensam.petkeeper.views.home.adapters;

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

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.models.PostHome;

public class HomeAdapterPost extends RecyclerView.Adapter<HomeAdapterPost.ViewHolder>{

    ArrayList<PostHome> allPosts;
    private HomeAdapterPost.ItemClickedListener mItemListener;

    public HomeAdapterPost(ArrayList<PostHome> petCategories, HomeAdapterPost.ItemClickedListener mItemListener) {
        //this.petCategories = petCategories;
        this.allPosts =  petCategories;
        this.mItemListener = mItemListener;
    }

    @NonNull
    @Override
    public HomeAdapterPost.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_home_post,parent,false);
        return new HomeAdapterPost.ViewHolder(inflate);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull HomeAdapterPost.ViewHolder holder, int position) {
        holder.postUserName.setText(allPosts.get(position).getUserName());
        holder.postPet.setText(allPosts.get(position).getPet());
        holder.postFrom.setText(allPosts.get(position).getFrom());
        holder.postTo.setText(allPosts.get(position).getTo());
        holder.postDuration.setText(allPosts.get(position).getDuration());
        holder.postGender.setText(allPosts.get(position).getGender());
        holder.postType.setText(allPosts.get(position).getType());
        holder.postDesc.setText(allPosts.get(position).getDescription());

        holder.constraintLayout.setOnClickListener(view -> {
            mItemListener.onClickItem(allPosts.get(position));
            notifyDataSetChanged();
        });
        holder.seePostLayout.setOnClickListener(view -> {
            mItemListener.onCLickSeePost(allPosts.get(position));
        });
        holder.postImage.setOnClickListener(view -> {
            mItemListener.onClickSeeProfile("OwnerId Or OwnerProfile");
        });

    }

    @Override
    public int getItemCount() {
        return allPosts.size();
    }

    public interface ItemClickedListener {
        void onClickItem(PostHome postHome);
        void onCLickSeePost(PostHome postHome);
        void onClickSeeProfile(String ownerProfileOrOwnerProfileId);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView postUserName, postPet, postType, postFrom, postTo, postGender, postDuration,postDesc;
        ImageView postImage;
        ConstraintLayout constraintLayout;
        LinearLayout seePostLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postUserName = itemView.findViewById(R.id.user_name_id);
            postDesc = itemView.findViewById(R.id.post_desc_id);
            postPet = itemView.findViewById(R.id.pet_id);
            postType = itemView.findViewById(R.id.type_id);
            postFrom = itemView.findViewById(R.id.from_id);
            postTo = itemView.findViewById(R.id.to_id);
            postGender = itemView.findViewById(R.id.gender_id);
            postDuration = itemView.findViewById(R.id.duration_id);
            postImage = itemView.findViewById(R.id.post_profile_image_id);
            constraintLayout = itemView.findViewById(R.id.post_constraint_id);
            seePostLayout = itemView.findViewById(R.id.see_post_id);

        }
    }
}
