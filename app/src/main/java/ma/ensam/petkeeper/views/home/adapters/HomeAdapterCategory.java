package ma.ensam.petkeeper.views.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.models.PetCategory;

public class HomeAdapterCategory extends RecyclerView.Adapter<HomeAdapterCategory.ViewModel> {

    ArrayList<PetCategory> allCategories;
    private ItemClickedListener mItemListener;

    public HomeAdapterCategory(ArrayList<PetCategory> petCategories, ItemClickedListener mItemListener) {
        //this.petCategories = petCategories;
        this.allCategories =  petCategories;
        this.mItemListener = mItemListener;
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new ViewModel(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, int position) {
        holder.categoryName.setText(allCategories.get(position).getName());
        String imgUrl = allCategories.get(position).getImg();
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(
                imgUrl,
                "drawable",
                holder.itemView.getContext().getPackageName());


        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.categoryImg);

        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(allCategories.get(position));
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public interface  ItemClickedListener{
        void onItemClick(PetCategory petCategory);
    }

    public static class ViewModel extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryImg;
        ConstraintLayout constraintLayout;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            categoryName= itemView.findViewById(R.id.category_text_id);
            categoryImg = itemView.findViewById(R.id.category_img_id);
            constraintLayout = itemView.findViewById(R.id.constraint_id);
        }


    }
}
