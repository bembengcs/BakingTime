package net.mavenmobile.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.model.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bembengcs on 8/20/2017.
 */

public class IngredietAdapter extends RecyclerView.Adapter<IngredietAdapter.MyViewHolder> {

    private List<Ingredient> mIngredientList;
    private Context mContext;

    public IngredietAdapter(List<Ingredient> ingredientList, Context context) {
        mIngredientList = ingredientList;
        mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_quantitiy)
        TextView mTvQuantitiy;
        @BindView(R.id.tv_measure)
        TextView mTvMeasure;
        @BindView(R.id.tv_ingridient)
        TextView mTvIngridient;

        private Ingredient mIngredient;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Ingredient ingredient) {
            DecimalFormat format = new DecimalFormat();
            mTvQuantitiy.setText(format.format(ingredient.getQuantity()) + " ");
            mTvMeasure.setText(ingredient.getMeasure());
            mTvIngridient.setText(ingredient.getIngredient());

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_ingredient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mIngredientList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) {
            return 0;
        }
        return mIngredientList.size();
    }

}
