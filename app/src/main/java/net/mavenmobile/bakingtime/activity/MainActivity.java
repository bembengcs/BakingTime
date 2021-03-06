package net.mavenmobile.bakingtime.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.adapter.RecipeAdapter;
import net.mavenmobile.bakingtime.idlingresource.SimpleIdlingResource;
import net.mavenmobile.bakingtime.model.Recipe;
import net.mavenmobile.bakingtime.rest.ApiClient;
import net.mavenmobile.bakingtime.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {

    @BindView(R.id.rv_recipe_card)
    RecyclerView mRvRecipeCard;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recipe_layout_container)
    LinearLayout mLinearLayout;
    private ApiInterface apiService;
    private RecipeAdapter mRecipeCardAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvRecipeCard.setLayoutManager(mLinearLayoutManager);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        loadAllRecipes();
    }

    private void loadAllRecipes() {
        Call<List<Recipe>> call = apiService.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                setupRecipeAdapter(recipes);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to connect, you need internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setupRecipeAdapter(List<Recipe> recipes) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRvRecipeCard.setLayoutManager(linearLayoutManager);
            mRecipeCardAdapter = new RecipeAdapter(recipes, this, this);
            mRvRecipeCard.setAdapter(mRecipeCardAdapter);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRvRecipeCard.setLayoutManager(gridLayoutManager);
            mRecipeCardAdapter = new RecipeAdapter(recipes, this, this);
            mRvRecipeCard.setAdapter(mRecipeCardAdapter);
        }
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }
}
