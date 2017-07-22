package everyday.sukhajata.com.everydayenglish.model;

/**
 * Created by Tim on 4/03/2017.
 */

public class User {
    public int Id;
    public String Email;
    public int CurrentLessonId;

    public User (int id, String email, int currentLessonId) {
        Id = id;
        Email = email;
        CurrentLessonId = currentLessonId;
    }
}
