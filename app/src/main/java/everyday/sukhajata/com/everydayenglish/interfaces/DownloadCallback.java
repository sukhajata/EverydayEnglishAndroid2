package everyday.sukhajata.com.everydayenglish.interfaces;

/**
 * Created by Tim on 18/02/2017.
 */

public interface DownloadCallback {
    String TYPE_LESSONS = "Lessons";
    String TYPE_SLIDES = "Slides";
    String TYPE_IMAGES = "Images";
    String TYPE_USER = "User";
    String TYPE_PUSH_LESSON_COMPLETED = "PushLessonCompleted";
    String TYPE_PULL_LESSONS_COMPLETED = "PullLessonsCompleted";
    String TYPE_TOTALS = "Totals";

    String RESPONSE_OK = "Ok";
    String RESPONSE_ERROR = "Error";


    void onDownloadError(String msg);
    void onDownloadFinished(String code, String type) ;
    void onDownloadResult(String code, String type, String[] args);
}
