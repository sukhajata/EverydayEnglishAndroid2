package everyday.sukhajata.com.everydayenglish.interfaces;

/**
 * Created by Tim on 8/04/2017.
 */

public interface AudioSetupCallback {
    int AUDIO_SETUP_SUCCESS = 1;
    int AUDIO_SETUP_MISSING_LANGUAGE = 2;
    int AUDIO_SETUP_FAILURE = 3;

    void onAudioSetupComplete(int code);
}
