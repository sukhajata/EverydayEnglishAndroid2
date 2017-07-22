package everyday.sukhajata.com.everydayenglish.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Tim on 30/03/2017.
 */

public class SlideWord implements Parcelable, Comparable<SlideWord> {
    public int Id;
    public int SlideId;
    public String English;
    public String Thai;
    public int WordOrder;
    public ArrayList<Phonetic> PhoneticList;

    public SlideWord(){}

    public SlideWord(int id, int slideId, String english, String thai, int wordOrder) {
        Id = id;
        SlideId = slideId;
        English = english;
        Thai = thai;
        WordOrder = wordOrder;

    }

    private SlideWord(Parcel in) {
        Id = in.readInt();
        SlideId = in.readInt();
        English = in.readString();
        Thai = in.readString();
        WordOrder = in.readInt();
        PhoneticList = new ArrayList<>();
        in.readTypedList(PhoneticList, Phonetic.CREATOR);
    }

    public int compareTo(SlideWord other) {
        if (this.WordOrder < other.WordOrder) {
            return -1;
        }

        return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(Id);
        out.writeInt(SlideId);
        out.writeString(English);
        out.writeString(Thai);
        out.writeInt(WordOrder);
        out.writeTypedList(PhoneticList);
    }

    public static final Parcelable.Creator<SlideWord> CREATOR =
            new Parcelable.Creator<SlideWord>() {
              @Override
                public SlideWord createFromParcel(Parcel in) {
                  return new SlideWord(in);
                }

              @Override
                public SlideWord[] newArray(int size) {
                  return new SlideWord[size];
              }
            };

}
