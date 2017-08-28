package net.mavenmobile.bakingtime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.adapter.IngredietAdapter;
import net.mavenmobile.bakingtime.adapter.StepAdapter;
import net.mavenmobile.bakingtime.fragment.RecipeDetailFragment;
import net.mavenmobile.bakingtime.model.Ingredient;
import net.mavenmobile.bakingtime.model.Recipe;
import net.mavenmobile.bakingtime.model.Step;
import net.mavenmobile.bakingtime.rest.ApiClient;
import net.mavenmobile.bakingtime.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements StepAdapter.OnItemClickListener {

    @BindView(R.id.rv_ingredient)
    RecyclerView mRvIngredient;
    @BindView(R.id.rv_step)
    RecyclerView mRvStep;
    @BindView(R.id.recipe_list_container)
    FrameLayout mRecipeListContainer;

    private ApiInterface apiService;
    private Recipe recipe;
    private String TAG = RecipeListActivity.class.getSimpleName();
    private IngredietAdapter mIngredientAdapter;
    private List<Ingredient> mIngredientList;
    private StepAdapter mStepAdapter;
    private List<Step> mStepList = new ArrayList<>();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        recipe = getIntent().getExtras().getParcelable("recipe");

        mIngredientList = new ArrayList<>();
        mIngredientList.addAll(recipe.getIngredients());
        mStepList = new ArrayList<>();
        mStepList.addAll(recipe.getSteps());
        mRvIngredient.setNestedScrollingEnabled(false);
        mRvStep.setNestedScrollingEnabled(false);

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
    public void onItemClick(Step step) {
        Step step = mStepList = new ArrayList<>();
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable("step", step);
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            FragmentManager fm = getSupportFragmentManager();
            detailFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.item_detail_container, detailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            Bundle args = new Bundle();
            args.putParcelable("step", step);
            intent.putExtras(args);
            startActivity(intent);
        }
    }
}
