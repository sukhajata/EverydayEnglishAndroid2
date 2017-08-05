package everyday.sukhajata.com.everydayenglish.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Tim on 17/02/2017.
 */

public class Slide implements Parcelable{

    public int Id;
    public int LessonId;
    public int CatId;
    public String Content;
    public String ContentThai;
    public int SlideOrder;
    public String ImageFileName;
    public int LessonReference;
    public ArrayList<SlideMedia> MediaList;
    //public ArrayList<SlideWord> SlideWordList;

    public Slide(){

    }
    
    public Slide(int id, int lessonId, int catId, String content,
                 String contentThai, int slideOrder, String imageFileName, int lessonReference,
                 ArrayList<SlideMedia> mediaList) {
        Id = id;
        LessonId = lessonId;
        CatId = catId;
        Content = content;
        ContentThai = contentThai;
        SlideOrder = slideOrder;
        ImageFileName = imageFileName;
        LessonReference = lessonReference;
        MediaList = mediaList;
        //SlideWordList = slideWordList;
       // IsCompleted = false;
    }

    private Slide(Parcel in) {
        Id = in.readInt();
        LessonId = in.readInt();
        CatId = in.readInt();
        Content = in.readString();
        ContentThai = in.readString();
        SlideOrder = in.readInt();
        ImageFileName = in.readString();
        LessonReference = in.readInt();
        MediaList = new ArrayList<>();
        in.readTypedList(MediaList, SlideMedia.CREATOR);
        //SlideWordList = new ArrayList<>();
        //in.readTypedList(SlideWordList, SlideWord.CREATOR);
    }

    public int getLayoutResId() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(Id);
        out.writeInt(LessonId);
        out.writeInt(CatId);
        out.writeString(Content);
        out.writeString(ContentThai);
        out.writeInt(SlideOrder);
        out.writeString(ImageFileName);
        out.writeInt(LessonReference);
        out.writeTypedList(MediaList);
        //out.writeTypedList(SlideWordList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Slide> CREATOR
            = new Parcelable.Creator<Slide>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Slide createFromParcel(Parcel in) {
            return new Slide(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Slide[] newArray(int size) {
            return new Slide[size];
        }
    };

}
