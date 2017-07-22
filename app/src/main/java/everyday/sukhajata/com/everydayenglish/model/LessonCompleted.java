package everyday.sukhajata.com.everydayenglish.model;

import android.content.Context;

/**
 * Created by Administrator on 22/7/2560.
 */

public class LessonCompleted {
    public int UserId;
    public int ModuleId;
    public int LessonCompletedOrder;
    public int Correct;
    public int Errors;
    public String DateCompleted;

    public LessonCompleted(int userId, int moduleId, int lessonCompletedOrder, int correct, int errors, String dateCompleted) {
        UserId = userId;
        ModuleId = moduleId;
        LessonCompletedOrder = lessonCompletedOrder;
        Correct = correct;
        Errors = errors;
        DateCompleted = dateCompleted;
    }
}
