package everyday.sukhajata.com.everydayenglish.jsonmapping;

import java.util.List;

import everyday.sukhajata.com.everydayenglish.model.Media;

/**
 * Created by Tim on 19/02/2017.
 */

public class JsonLesson {
    public int Id;
    public int ModuleId;
    public String Name;
    public String Description;
    public int LessonOrder;
    public List<Media> LessonMedia;
}
