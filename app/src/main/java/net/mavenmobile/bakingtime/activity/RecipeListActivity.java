package net.mavenmobile.bakingtime.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.adapter.IngredietAdapter;
import net.mavenmobile.bakingtime.adapter.StepAdapter;
import net.mavenmobile.bakingtime.config.Constants;
import net.mavenmobile.bakingtime.fragment.RecipeDetailFragment;
import net.mavenmobile.bakingtime.model.Ingredient;
import net.mavenmobile.bakingtime.model.Recipe;
import net.mavenmobile.bakingtime.model.Step;
import net.mavenmobile.bakingtime.widget.IngredientsAppWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements StepAdapter.OnItemClickListener {

    @BindView(R.id.rv_ingredient)
    RecyclerView mRvIngredient;
    @BindView(R.id.rv_step)
    RecyclerView mRvStep;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.recipe_list_container)
    FrameLayout mRecipeListContainer;

    private String TAG = RecipeListActivity.class.getSimpleName();
    private Recipe recipe;
    private boolean mTwoPane;
    private List<Ingredient> mIngredientList;
    private IngredietAdapter mIngredientAdapter;
    private ArrayList<Ingredient> ingredientsArray = new ArrayList<>();
    private List<Step> mStepList;
    private StepAdapter mStepAdapter;
    private ArrayList<Step> stepArray = new ArrayList<>();
    private final String EXTRA_KEY_CURRENT_POSITION = "current_position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        recipe = getIntent().getExtras().getParcelable("recipe");

        mIngredientList = new ArrayList<>();
        mIngredientList.addAll(recipe.getIngredients());
        mStepList = new ArrayList<>();
        mStepList.addAll(recipe.getSteps());
        mRvIngredient.setNestedScrollingEnabled(false);
        mRvStep.setNestedScrollingEnabled(false);

        if (savedInstanceState != null) {

        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        setupIngredientAdapter();
        setupStepAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(EXTRA_KEY_CURRENT_POSITION,
                new int[]{mNestedScrollView.getScrollX(), mNestedScrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("RECIPE_SCROLL_POSITION");
        if (position != null)
            mNestedScrollView.post(new Runnable() {
                public void run() {
                    mNestedScrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    private void setupIngredientAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvIngredient.setLayoutManager(layoutManager);
        mIngredientAdapter = new IngredietAdapter(mIngredientList, this);
        mRvIngredient.setAdapter(mIngredientAdapter);
    }

    private void setupStepAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvStep.setLayoutManager(layoutManager);
        mStepAdapter = new StepAdapter(mStepList, this, this);
        mRvStep.setAdapter(mStepAdapter);
    }

    @Override
    public void onItemClick(int position) {
        stepArray.addAll(mStepList);

        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelableArrayList("stepList", stepArray);
            args.putInt("position", position);
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            FragmentManager fm = getSupportFragmentManager();
            detailFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.item_detail_container, detailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("stepList", stepArray);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    private void updateWidget() {
        Constants.widget.clear();
        ingredientsArray.addAll(mIngredientList);
        Constants.widget = ingredientsArray;

        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(getApplicationContext(), IngredientsAppWidget.class));
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);

        Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_widget:
                updateWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
