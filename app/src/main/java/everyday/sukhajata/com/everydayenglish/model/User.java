package everyday.sukhajata.com.everydayenglish.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbContract;

/**
 * Created by Tim on 4/03/2017.
 */

public class User implements Parcelable{
    public int Id;
    public String Email;
    public int StudentPosition;
    public int StudentId;
    public String Gender;
    public String FirstName;
    public String LastName;
    public String Password;
    public int ClassId;
    public int ModuleId;
    public int LessonCompletedOrder;
    public int Active;

    public User (int id, String email, int lessonCompletedOrder) {
        Id = id;
        Email = email;
        LessonCompletedOrder = lessonCompletedOrder;
    }

    public User(int id, int studentPosition, int studentId, String gender, String firstName, String lastName,
                 String password, int classId, int moduleId, int lessonCompletedOrder, int active) {
        Id = id;
        Email = "";
        StudentPosition = studentPosition;
        StudentId = studentId;
        Gender = gender;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
        ClassId = classId;
        ModuleId = moduleId;
        LessonCompletedOrder = lessonCompletedOrder;
        Active = active;

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public User(){

    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(Id);
        out.writeString(Email);
        out.writeInt(StudentPosition);
        out.writeInt(StudentId);
        out.writeString(Gender);
        out.writeString(FirstName);
        out.writeString(LastName);
        out.writeString(Password);
        out.writeInt(ClassId);
        out.writeInt(ModuleId);
        out.writeInt(LessonCompletedOrder);
        out.writeInt(Active);

    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private User(Parcel in) {
        Id = in.readInt();
        Email = in.readString();
        StudentPosition = in.readInt();
        StudentId = in.readInt();
        Gender = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        Password = in.readString();
        ClassId = in.readInt();
        ModuleId = in.readInt();
        LessonCompletedOrder = in.readInt();
        Active = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
