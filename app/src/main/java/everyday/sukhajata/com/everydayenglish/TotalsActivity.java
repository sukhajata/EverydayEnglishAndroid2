package everyday.sukhajata.com.everydayenglish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import everyday.sukhajata.com.everydayenglish.interfaces.NextLessonListener;

import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_FINISH_TYPE;
import static everyday.sukhajata.com.everydayenglish.LessonActivity.FINISH_TYPE_LESSON_COMPLETED;

public class TotalsActivity extends AppCompatActivity {

    public static final String ARG_NAME_TOTAL = "Total";


    private int mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totals);

        Bundle bundle = getIntent().getExtras();
        mTotal = bundle.getInt(ARG_NAME_TOTAL);

        TextView txtTotals = (TextView)findViewById(R.id.totals_wordTotal);
        txtTotals.setText(getString(R.string.word_total_label) + ": " +
            String.valueOf(mTotal));

        Button btnNext = (Button)findViewById(R.id.totals_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.putExtra(ARG_NAME_FINISH_TYPE, FINISH_TYPE_LESSON_COMPLETED);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }






}
