package net.mavenmobile.bakingtime.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.fragment.RecipeDetailFragment;
import net.mavenmobile.bakingtime.model.Recipe;
import net.mavenmobile.bakingtime.model.Step;
import net.mavenmobile.bakingtime.rest.ApiClient;
import net.mavenmobile.bakingtime.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    private ApiInterface apiService;
    private Step step;
    private List<Step> mStepList = new ArrayList<>();
    private Recipe recipe;
    private String TAG = RecipeDetailActivity.class.getSimpleName();
    private FragmentPagerAdapter mAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        step = getIntent().getExtras().getParcelable("step");

        Bundle args = new Bundle();
        args.putParcelable("step", step);

        if (savedInstanceState == null) {
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            FragmentManager fm = getSupportFragmentManager();
            detailFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.fragment_container, detailFragment)
                    .commit();
        }


    }
}
