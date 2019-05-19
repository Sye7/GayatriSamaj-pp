package com.example.dell.jaapactivity.Jap;

import android.provider.BaseColumns;

public class JapContractClass {
    public static final class WaitListEntry implements BaseColumns {
        public static final String TABLE_JAP_DATA = "japData";
        public static final String KEY_ID = "id";
        public static final String KEY_TYPE = "type";
        public static final String KEY_TIME = "time";
        public static final String HAS_VIDEO = "hasVideo";
        public static final String VIDEO_URL = "videoUrl";

    }
}
