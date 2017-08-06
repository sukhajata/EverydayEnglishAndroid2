package everyday.sukhajata.com.everydayenglish;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import everyday.sukhajata.com.everydayenglish.ClassListFragment.ClassListFragmentInteractionListener;
import everyday.sukhajata.com.everydayenglish.dummy.DummyContent.DummyItem;
import everyday.sukhajata.com.everydayenglish.model.User;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link ClassListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ClassListRecyclerViewAdapter extends RecyclerView.Adapter<ClassListRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final ClassListFragmentInteractionListener mListener;

    public ClassListRecyclerViewAdapter(List<User> items, ClassListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.user = mValues.get(position);
        holder.mClassNameView.setText(mValues.get(position).ClassId);
        holder.mFirstNameView.setText(mValues.get(position).FirstName);
        holder.mLastNameView.setText(mValues.get(position).LastName);
        holder.mLessonOrderView.setText(mValues.get(position).LessonCompletedOrder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClassListFragmentInteraction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mClassNameView;
        public final TextView mFirstNameView;
        public final TextView mLastNameView;
        public final TextView mLessonOrderView;
        public final TextView mAccuracyView;
        public User user;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mClassNameView = (TextView) view.findViewById(R.id.class_name);
            mFirstNameView = (TextView) view.findViewById(R.id.class_firstName);
            mLastNameView = (TextView)view.findViewById(R.id.class_lastName);
            mLessonOrderView = (TextView)view.findViewById(R.id.class_lessonOrder);
            mAccuracyView = (TextView)view.findViewById(R.id.class_accuracy);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFirstNameView.getText() + "'";
        }
    }
}
