package ma.ensam.petkeeper.views.profile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.app.AppConfig;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.models.PostHome;
import ma.ensam.petkeeper.models.PostProfile;
import ma.ensam.petkeeper.models.ReviewProfile;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;
import ma.ensam.petkeeper.views.home.adapters.HomeAdapterPost;

public class ProfileAdapterPost extends RecyclerView.Adapter<ProfileAdapterPost.ViewHolder> {
    private List<PostProfile> profilePosts;
    private ItemClickedListener mItemClickListener;

    public ProfileAdapterPost(List<PostProfile> profilePosts, ItemClickedListener mItemClickListener) {
        this.profilePosts = profilePosts;
        this.mItemClickListener = mItemClickListener;
    }

    public void setProfilePosts(List<PostProfile> profilePosts) {
        this.profilePosts = profilePosts;
    }

    @NonNull
    @Override
    public ProfileAdapterPost.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ProfileAdapterPost.ViewHolder(inflated);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapterPost.ViewHolder holder, int position) {
        holder.cardProfileImage.setImageBitmap(
                BitmapUtility.extractFromPath(profilePosts.get(position).getProfileUrl())
        );
        holder.cardCreationDate.setText(
                AppConfig.dateFormat.format(profilePosts.get(position).getCreationDate())
        );
        holder.cardFromDate.setText(
                AppConfig.dateFormat.format(profilePosts.get(position).getFrom())
        );
        holder.cardToDate.setText(
                AppConfig.dateFormat.format(profilePosts.get(position).getTo())
        );
        holder.cardProfileName.setText(profilePosts.get(position).getUserName());
        holder.cardTitle.setText(profilePosts.get(position).getTitre());
        holder.petSpecies.setText(profilePosts.get(position).getPetSpecies());
        holder.petSpecies.setText(profilePosts.get(position).getPetSpecies());

        holder.cardInner.setOnClickListener(view -> {
            mItemClickListener.onItemClick(profilePosts.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return profilePosts.size();
    }

    public interface ItemClickedListener {
        void onItemClick(PostProfile postProfile);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout cardInner;
        private final TextView cardProfileName;
        private final TextView cardCreationDate;
        private final TextView cardTitle;
        private final TextView petSpecies;
        private final TextView cardFromDate;
        private final TextView cardToDate;
        private final ShapeableImageView cardProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardInner = (ConstraintLayout) ((FrameLayout) itemView).getChildAt(0);
            cardProfileImage = cardInner.findViewById(R.id.card_profile_picture);
            cardProfileName = cardInner.findViewById(R.id.card_profile_name);
            cardCreationDate = cardInner.findViewById(R.id.card_profile_date);
            cardTitle = cardInner.findViewById(R.id.card_title);
            petSpecies = cardInner.findViewById(R.id.card_pet_value);
            cardFromDate = cardInner.findViewById(R.id.card_from_value);
            cardToDate = cardInner.findViewById(R.id.card_to_value);
        }
    }
}
