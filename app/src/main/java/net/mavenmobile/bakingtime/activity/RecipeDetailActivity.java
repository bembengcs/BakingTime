package net.mavenmobile.bakingtime.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.fragment.RecipeDetailFragment;
import net.mavenmobile.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    private Step step;
    private List<Step> mStepList = new ArrayList<>();
    private String TAG = RecipeDetailActivity.class.getSimpleName();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", -1);
        ArrayList<Step> stepArray = getIntent().getParcelableArrayListExtra("stepList");
        mStepList.addAll(stepArray);

        step = mStepList.get(position);

        if (savedInstanceState == null) {
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            FragmentManager fm = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("stepList", stepArray);
            bundle.putInt("position", position);
            detailFragment.setArguments(bundle);
            fm.beginTransaction()
                    .add(R.id.fragment_container, detailFragment)
                    .commit();
        }


    }
}
