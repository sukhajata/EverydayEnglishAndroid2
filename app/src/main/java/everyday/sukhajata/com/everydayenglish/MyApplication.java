package everyday.sukhajata.com.everydayenglish;


import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

import everyday.sukhajata.com.everydayenglish.interfaces.AudioSetupCallback;

/**
 * Created by Administrator on 18/8/2560.
 */

public class MyApplication extends Application {

    TextToSpeech textToSpeech;
    boolean initialized;
    boolean missingData;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void setupAudio(final AudioSetupCallback callback) {
        textToSpeech = new TextToSpeech(
                this,
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            int result = textToSpeech.setLanguage(Locale.US);
                            if (result == TextToSpeech.LANG_MISSING_DATA ) {
                                callback.onAudioSetupComplete(AudioSetupCallback.AUDIO_SETUP_MISSING_LANGUAGE);
                            } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                callback.onAudioSetupComplete(AudioSetupCallback.AUDIO_SETUP_FAILURE);
                            } else {
                                textToSpeech.setSpeechRate(0.8f);
                                callback.onAudioSetupComplete(AudioSetupCallback.AUDIO_SETUP_SUCCESS);
                                initialized = true;
                            }
                        } else {
                            callback.onAudioSetupComplete(AudioSetupCallback.AUDIO_SETUP_FAILURE);
                        }
                    }
                },
                "com.google.android.tts");
    }


    public void playAudio(String text) {

        if (initialized) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.v("TTS", "Speak new API");
                Bundle bundle = new Bundle();
                bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, null);
            } else {
                Log.v("TTS", "Speak old API");
                HashMap<String, String> param = new HashMap<>();
                param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, param);
            }
        }

    }

}
