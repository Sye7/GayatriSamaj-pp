package com.example.dell.jaapactivity.Meditation;

import android.provider.BaseColumns;

public class MeditationContractClass {
    public static final class WaitListEntryMed implements BaseColumns {

        public static final String TABLE_MEDITATION_DATA = "meditationData";
        public static final String KEY_ID = "id";
        public static final String KEY_AUDIO_NAME = "audioName";
        public static final String KEY_AUDIO_DURATION = "audioDuration";

    }
}
