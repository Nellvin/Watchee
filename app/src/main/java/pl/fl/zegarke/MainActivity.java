package pl.fl.zegarke;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GridViewPager gridViewPager = (GridViewPager) findViewById(R.id.pager);
        gridViewPager.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                boolean round = insets.isRound();
                int rowMargin = getResources().getDimensionPixelSize(R.dimen.page_row_margin);
                int colMargin = getResources().getDimensionPixelSize(round? R.dimen.page_column_margin:R.dimen.page_column_margin);
                gridViewPager.setPageMargins(rowMargin,colMargin);
                gridViewPager.onApplyWindowInsets(insets);
                return insets;
            }
        });
        gridViewPager.setAdapter(new MyGridAdapter(this, getFragmentManager()));
        DotsPageIndicator dotsPageIndicator= (DotsPageIndicator)findViewById(R.id.page_ind);
        dotsPageIndicator.setPager(gridViewPager);

        // Enables Always-on
        setAmbientEnabled();
    }
}
