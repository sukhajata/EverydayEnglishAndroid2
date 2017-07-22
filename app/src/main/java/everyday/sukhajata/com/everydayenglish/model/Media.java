package everyday.sukhajata.com.everydayenglish.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;

/**
 * Created by Tim on 18/02/2017.
 */

public class Media implements Parcelable {

    public int Id;
    public String English;
    public String Thai;
    public String ImageFileName;
    public String AudioFileName;
    public String Notes;
    public boolean DownloadComplete;

    public Media(){

    }

    public Media(int id, String english, String thai, String imageFileName,
                 String audioFileName, String notes) {
        Id = id;
        English = english;
        Thai = thai;
        ImageFileName = imageFileName;
        AudioFileName = audioFileName;
        Notes = notes;
        DownloadComplete = false;
    }

    private Media(Parcel in) {
        Id = in.readInt();
        English = in.readString();
        Thai = in.readString();
        ImageFileName = in.readString();
        AudioFileName = in.readString();
        Notes = in.readString();
        DownloadComplete = in.readByte() != 0;
        //IsTarget = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(Id);
        out.writeString(English);
        out.writeString(Thai);
        out.writeString(ImageFileName);
        out.writeString(AudioFileName);
        out.writeString(Notes);
        out.writeByte((byte)(DownloadComplete ? 1 : 0));
    }


    public static final Parcelable.Creator<Media> CREATOR
            = new Parcelable.Creator<Media>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };


}
