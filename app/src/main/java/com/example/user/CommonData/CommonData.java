package com.example.user.CommonData;

public class CommonData {

    public static final String SERVER_IP = "http://220.135.192.24";

    public static final String LOG_IN_API = "login.php";

    public static final String REGISTER_API = "register.php";

    public static final String QUERY_STORE_NAME_API = "query_store_name.php";

    public static final String CALL_NUM_API = "callnum.php";

    public static final String QUERY_WAIT_NUM_API = "query_num_all.php";

    public static final String QUERY_LAST_WAIT_NUM_API = "query_last_num.php";

    public static final String INIT_API = "init.php";

    public static final String QUERY_TIME_STAMP_API = "query_time_stamp.php";

    public static final String TABLE_NAME = "test_info";

    public static String TIME_STAMP;

    public static String getTimeStamp() {
        return TIME_STAMP;
    }

    public static void setTimeStamp(String timeStamp) {
        TIME_STAMP = timeStamp;
    }
}
