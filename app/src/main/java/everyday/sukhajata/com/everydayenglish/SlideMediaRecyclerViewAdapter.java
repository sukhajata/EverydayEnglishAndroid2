package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import everyday.sukhajata.com.everydayenglish.ItemFragment.OnLessonFragmentInteractionListener;
//import everyday.sukhajata.com.everydayenglish.dummy.DummyContent.DummyItem;
import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link SlideCompletedListener}.
 */
public class SlideMediaRecyclerViewAdapter extends RecyclerView.Adapter<SlideMediaRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<SlideMedia> mValues;
    private final SlideCompletedListener mListener;

    public SlideMediaRecyclerViewAdapter(Context context, List<SlideMedia> items, SlideCompletedListener listener) {
        mContext = context;
        mValues = items;
        Collections.sort(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_teaching, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mEnglishView.setText(mValues.get(position).English);
        holder.mThaiView.setText(mValues.get(position).Thai);
        if (mValues.get(position).Notes.length() > 0) {
            holder.mNotesView.setText(mValues.get(position).Notes);
        } else {
            holder.mNotesView.setVisibility(View.GONE);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onSlideCompleted(holder.mItem.SlideId);
                    ContentManager.playAudio(holder.mItem.English, holder.mItem.AudioFileName);

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
        public final TextView mEnglishView;
        public final TextView mThaiView;
        public final TextView mNotesView;
        public SlideMedia mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mEnglishView = (TextView) view.findViewById(R.id.teaching_item_english);
            mThaiView = (TextView) view.findViewById(R.id.teaching_item_thai);
            mNotesView = (TextView) view.findViewById(R.id.teaching_item_notes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEnglishView.getText() + "'";
        }
    }
}
