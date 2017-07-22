package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import everyday.sukhajata.com.everydayenglish.LessonFragment.OnLessonFragmentInteractionListener;
import everyday.sukhajata.com.everydayenglish.dummy.DummyContent.DummyItem;
import everyday.sukhajata.com.everydayenglish.model.Lesson;
import everyday.sukhajata.com.everydayenglish.model.LessonCompleted;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnLessonFragmentInteractionListener}.
 */
public class MyLessonRecyclerViewAdapter extends RecyclerView.Adapter<MyLessonRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private int mUserId;
    private int mModuleId;
    private final List<Lesson> mLessons;
    private final LessonFragment.OnLessonFragmentInteractionListener mListener;

    public MyLessonRecyclerViewAdapter(Context context,
                                       int userId,
                                       int moduleId,
                                       ArrayList<Lesson> lessons,
                                       LessonFragment.OnLessonFragmentInteractionListener listener){

        mContext = context;
        mUserId = userId;
        mModuleId = moduleId;
        mLessons = lessons;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mLessons.get(position);
        holder.mNameView.setText(mLessons.get(position).Name);
        holder.mDescriptionView.setText(mLessons.get(position).Description);

        LessonCompleted lessonCompleted = EverydayLanguageDbHelper
                .getInstance(mContext)
                .getLessonCompleted(mUserId, mModuleId, holder.mItem.LessonOrder);

        if (lessonCompleted != null) {
            holder.mCorrectView.setText(lessonCompleted.Correct);
            holder.mErrorsView.setText(lessonCompleted.Errors);
            holder.mDateCompletedView.setText(lessonCompleted.DateCompleted);

            //set duller background colors
            holder.mTitleBar.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimaryLight, null));
            holder.mDescriptionView.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorLightGrey, null));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final RelativeLayout mTitleBar;
        public final TextView mNameView;
        public final TextView mErrorsView;
        public final TextView mCorrectView;
        public final TextView mDateCompletedView;
        public final TextView mDescriptionView;
        public Lesson mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleBar = (RelativeLayout) view.findViewById(R.id.lesson_titleBar);
            mNameView = (TextView)view.findViewById(R.id.lesson_txtName);
            mErrorsView = (TextView)view.findViewById(R.id.lesson_txtErrors);
            mCorrectView = (TextView)view.findViewById(R.id.lesson_txtCorrect);
            mDateCompletedView = (TextView)view.findViewById(R.id.lesson_txtDateCompleted);
            mDescriptionView = (TextView)view.findViewById(R.id.lesson_txtDescription);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
