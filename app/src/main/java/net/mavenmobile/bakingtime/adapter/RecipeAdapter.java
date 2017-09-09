package net.mavenmobile.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bembengcs on 8/19/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private List<Recipe> mRecipeList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public RecipeAdapter(List<Recipe> recipeList, Context context, OnItemClickListener onItemClickListener) {
        mRecipeList = recipeList;
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cake)
        ImageView mIvCake;
        @BindView(R.id.tv_recipe_name)
        TextView mTvRecipeName;
        @BindView(R.id.tv_servings)
        TextView mTvServings;
        @BindView(R.id.relative_layout)
        LinearLayout mRelativeLayout;
        @BindView(R.id.cv_recipe_list)
        CardView mCvRecipeList;
        @BindView(R.id.tv_ingredients)
        TextView mTvIngredients;
        @BindView(R.id.tv_steps)
        TextView mTvSteps;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Recipe recipe) {
            Glide.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.ic_insert_photo)
                    .error(R.drawable.ic_broken_image)
                    .into(mIvCake);
            mTvRecipeName.setText(recipe.getName());
            mTvServings.setText("Serves: " + String.valueOf(recipe.getServings()));
            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(recipe);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_recipe_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mRecipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

}
