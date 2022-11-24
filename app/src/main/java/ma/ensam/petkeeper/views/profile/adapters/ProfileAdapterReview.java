package ma.ensam.petkeeper.views.profile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.app.AppConfig;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.models.PetCategory;
import ma.ensam.petkeeper.models.PostProfile;
import ma.ensam.petkeeper.models.ReviewProfile;
import ma.ensam.petkeeper.utils.BitmapUtility;

public class ProfileAdapterReview extends RecyclerView.Adapter<ProfileAdapterReview.ViewHolder> {
    private List<ReviewProfile> reviewProfiles;
    private ItemClickedListener mItemClickListener;

    public ProfileAdapterReview(List<ReviewProfile> profilePosts, ItemClickedListener mItemClickListener) {
        this.reviewProfiles = profilePosts;
        this.mItemClickListener = mItemClickListener;
    }

    public void setProfilePosts(List<ReviewProfile> profilePosts) {
        this.reviewProfiles = profilePosts;
    }

    @NonNull
    @Override
    public ProfileAdapterReview.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
        return new ProfileAdapterReview.ViewHolder(inflated);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapterReview.ViewHolder holder, int position) {
        holder.reviewProfileImage.setImageBitmap(
                BitmapUtility.extractFromPath(reviewProfiles.get(position).getProfileUrl())
        );
        holder.reviewProfileName.setText(reviewProfiles.get(position).getUserName());
        holder.reviewText.setText(reviewProfiles.get(position).getReviewText());
        holder.reviewStars.setRating(reviewProfiles.get(position).getReviewStars());
        holder.reviewContainer.setOnClickListener(view -> {
            mItemClickListener.onItemClick(reviewProfiles.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return reviewProfiles.size();
    }

    public interface ItemClickedListener {
        void onItemClick(ReviewProfile reviewProfile);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout reviewContainer;
        private final TextView reviewProfileName;
        private final TextView reviewText;
        private final RatingBar reviewStars;

        private final ShapeableImageView reviewProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewContainer = (RelativeLayout) itemView;
            reviewProfileName = itemView.findViewById(R.id.profile_name_review);
            reviewProfileImage = itemView.findViewById(R.id.profile_picture_review);
            reviewText = itemView.findViewById(R.id.profile_review);
            reviewStars = itemView.findViewById(R.id.review_rating_stars);

        }
    }
}
