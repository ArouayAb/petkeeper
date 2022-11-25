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
import ma.ensam.petkeeper.models.OfferProfile;
import ma.ensam.petkeeper.utils.BitmapUtility;

public class ProfileAdapterOffer extends RecyclerView.Adapter<ProfileAdapterOffer.ViewHolder> {
    private List<OfferProfile> profileOffers;
    private ItemClickedListener mItemClickListener;

    public ProfileAdapterOffer(List<OfferProfile> profileOffers, ItemClickedListener mItemClickListener) {
        this.profileOffers = profileOffers;
        this.mItemClickListener = mItemClickListener;
    }

    public void updateProfileOffers(List<OfferProfile> profileOffers) {
        this.profileOffers = profileOffers;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfileAdapterOffer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflated = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ProfileAdapterOffer.ViewHolder(inflated);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapterOffer.ViewHolder holder, int position) {
        holder.cardProfileImage.setImageBitmap(
                BitmapUtility.extractFromPath(profileOffers.get(position).getProfileUrl())
        );
        holder.cardCreationDate.setText(
                AppConfig.dateFormat.format(profileOffers.get(position).getCreationDate())
        );
        holder.cardFromDate.setText(
                AppConfig.dateFormat.format(profileOffers.get(position).getFrom())
        );
        holder.cardToDate.setText(
                AppConfig.dateFormat.format(profileOffers.get(position).getTo())
        );
        holder.cardProfileName.setText(profileOffers.get(position).getUserName());
        holder.cardTitle.setText(profileOffers.get(position).getTitre());
        holder.petSpecies.setText(profileOffers.get(position).getPetSpecies());
        holder.petSpecies.setText(profileOffers.get(position).getPetSpecies());

        holder.rootView.setOnClickListener(view -> {
            mItemClickListener.onItemClick(profileOffers.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return profileOffers.size();
    }

    public interface ItemClickedListener {
        void onItemClick(OfferProfile postProfile);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout rootView;
        private final TextView cardProfileName;
        private final TextView cardCreationDate;
        private final TextView cardTitle;
        private final TextView petSpecies;
        private final TextView cardFromDate;
        private final TextView cardToDate;
        private final ShapeableImageView cardProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = (FrameLayout) itemView;

            View cardInner = (ConstraintLayout) ((FrameLayout) itemView).getChildAt(0);
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
