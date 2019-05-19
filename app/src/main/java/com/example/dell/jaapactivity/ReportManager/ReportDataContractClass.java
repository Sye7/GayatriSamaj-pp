package com.example.dell.jaapactivity.ReportManager;

import android.provider.BaseColumns;

public class ReportDataContractClass {
    public static final class WaitListEntryReport implements BaseColumns {
        public static final String TABLE_USER_REPORT = "userReport";
        public static final String KEY_ID = "id";
        public static final String KEY_MODE = "mode";
        public static final String KEY_USER_TIME = "user_time";
        public static final String KEY_ACTUAL_TIME = "actual_time";
        public static final String KEY_DATE = "date";
        public static final String KEY_TIME = "time";
        public static final String KEY_DAY = "day";
        public static final String KEY_TYPE = "type";
        public static final String KEY_AUDIO_NAME = "audioName";
        public static final String KEY_YEAR = "year";

    }

}
