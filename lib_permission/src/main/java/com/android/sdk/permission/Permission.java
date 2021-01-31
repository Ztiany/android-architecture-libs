package com.android.sdk.permission;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * copy from https://github.com/yanzhenjie/AndPermission
 */
public final class Permission {

    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    public static final String CAMERA = "android.permission.CAMERA";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String USE_SIP = "android.permission.USE_SIP";
    public static final String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";
    public static final String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";
    public static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";

    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";

    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";
    public static final String ACTIVITY_RECOGNITION = "android.permission.ACTIVITY_RECOGNITION";

    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final class Group {

        public static final String[] CALENDAR = new String[]{com.yanzhenjie.permission.runtime.Permission.READ_CALENDAR, com.yanzhenjie.permission.runtime.Permission.WRITE_CALENDAR};

        public static final String[] CAMERA = new String[]{com.yanzhenjie.permission.runtime.Permission.CAMERA};

        public static final String[] CONTACTS = new String[]{com.yanzhenjie.permission.runtime.Permission.READ_CONTACTS, com.yanzhenjie.permission.runtime.Permission.WRITE_CONTACTS, com.yanzhenjie.permission.runtime.Permission.GET_ACCOUNTS};

        public static final String[] LOCATION = new String[]{com.yanzhenjie.permission.runtime.Permission.ACCESS_FINE_LOCATION, com.yanzhenjie.permission.runtime.Permission.ACCESS_COARSE_LOCATION,
                com.yanzhenjie.permission.runtime.Permission.ACCESS_BACKGROUND_LOCATION};

        public static final String[] MICROPHONE = new String[]{com.yanzhenjie.permission.runtime.Permission.RECORD_AUDIO};

        public static final String[] PHONE = new String[]{com.yanzhenjie.permission.runtime.Permission.READ_PHONE_STATE, com.yanzhenjie.permission.runtime.Permission.CALL_PHONE, com.yanzhenjie.permission.runtime.Permission.USE_SIP,
                com.yanzhenjie.permission.runtime.Permission.READ_PHONE_NUMBERS, com.yanzhenjie.permission.runtime.Permission.ANSWER_PHONE_CALLS, com.yanzhenjie.permission.runtime.Permission.ADD_VOICEMAIL};

        public static final String[] CALL_LOG = new String[]{com.yanzhenjie.permission.runtime.Permission.READ_CALL_LOG, com.yanzhenjie.permission.runtime.Permission.WRITE_CALL_LOG,
                com.yanzhenjie.permission.runtime.Permission.PROCESS_OUTGOING_CALLS};

        public static final String[] SENSORS = new String[]{com.yanzhenjie.permission.runtime.Permission.BODY_SENSORS};

        public static final String[] ACTIVITY_RECOGNITION = new String[]{com.yanzhenjie.permission.runtime.Permission.ACTIVITY_RECOGNITION};

        public static final String[] SMS = new String[]{com.yanzhenjie.permission.runtime.Permission.SEND_SMS, com.yanzhenjie.permission.runtime.Permission.RECEIVE_SMS, com.yanzhenjie.permission.runtime.Permission.READ_SMS,
                com.yanzhenjie.permission.runtime.Permission.RECEIVE_WAP_PUSH, com.yanzhenjie.permission.runtime.Permission.RECEIVE_MMS};

        public static final String[] STORAGE = new String[]{com.yanzhenjie.permission.runtime.Permission.READ_EXTERNAL_STORAGE, com.yanzhenjie.permission.runtime.Permission.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String... permissions) {
        return transformText(context, Arrays.asList(permissions));
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        return transformText(context, permissionList);
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, List<String> permissions) {
        List<String> textList = new ArrayList<>();
        for (String permission : permissions) {
            switch (permission) {
                case com.yanzhenjie.permission.runtime.Permission.READ_CALENDAR:
                case com.yanzhenjie.permission.runtime.Permission.WRITE_CALENDAR: {
                    String message = context.getString(R.string.Permission_permission_name_calendar);

                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }

                case com.yanzhenjie.permission.runtime.Permission.CAMERA: {
                    String message = context.getString(R.string.Permission_permission_name_camera);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.GET_ACCOUNTS:
                case com.yanzhenjie.permission.runtime.Permission.READ_CONTACTS:
                case com.yanzhenjie.permission.runtime.Permission.WRITE_CONTACTS: {
                    String message = context.getString(R.string.Permission_permission_name_contacts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.ACCESS_FINE_LOCATION:
                case com.yanzhenjie.permission.runtime.Permission.ACCESS_COARSE_LOCATION: {
                    String message = context.getString(R.string.Permission_permission_name_accounts);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.RECORD_AUDIO: {
                    String message = context.getString(R.string.Permission_permission_name_microphone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.READ_PHONE_STATE:
                case com.yanzhenjie.permission.runtime.Permission.CALL_PHONE:
                case com.yanzhenjie.permission.runtime.Permission.ADD_VOICEMAIL:
                case com.yanzhenjie.permission.runtime.Permission.USE_SIP:
                case com.yanzhenjie.permission.runtime.Permission.READ_PHONE_NUMBERS:
                case com.yanzhenjie.permission.runtime.Permission.ANSWER_PHONE_CALLS: {
                    String message = context.getString(R.string.permission_name_phone);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.READ_CALL_LOG:
                case com.yanzhenjie.permission.runtime.Permission.WRITE_CALL_LOG:
                case com.yanzhenjie.permission.runtime.Permission.PROCESS_OUTGOING_CALLS: {
                    int messageId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ?
                            R.string.permission_name_call_log : R.string.permission_name_phone;
                    String message = context.getString(messageId);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.BODY_SENSORS: {
                    String message = context.getString(R.string.Permission_permission_name_sensors);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.ACTIVITY_RECOGNITION: {
                    String message = context.getString(R.string.permission_name_activity_recognition);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.SEND_SMS:
                case com.yanzhenjie.permission.runtime.Permission.RECEIVE_SMS:
                case com.yanzhenjie.permission.runtime.Permission.READ_SMS:
                case com.yanzhenjie.permission.runtime.Permission.RECEIVE_WAP_PUSH:
                case com.yanzhenjie.permission.runtime.Permission.RECEIVE_MMS: {
                    String message = context.getString(R.string.Permission_permission_name_sms);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
                case com.yanzhenjie.permission.runtime.Permission.READ_EXTERNAL_STORAGE:
                case com.yanzhenjie.permission.runtime.Permission.WRITE_EXTERNAL_STORAGE: {
                    String message = context.getString(R.string.Permission_permission_name_storage);
                    if (!textList.contains(message)) {
                        textList.add(message);
                    }
                    break;
                }
            }
        }
        return textList;
    }

} 