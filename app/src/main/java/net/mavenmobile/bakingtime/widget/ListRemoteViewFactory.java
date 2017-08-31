package net.mavenmobile.bakingtime.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.config.Constants;
import net.mavenmobile.bakingtime.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by bembengcs on 8/30/2017.
 */

public class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private String TAG = ListRemoteViewFactory.class.getSimpleName();
    private Context context;
    private ArrayList<Ingredient> ingredients;

    public ListRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredients = Constants.widget;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredients.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        views.setTextViewText(R.id.ingredient_name, ingredient.getIngredient());
        views.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantity() + " " + ingredient.getMeasure());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return Constants.VIEW_TYPE_IN_LIST;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
