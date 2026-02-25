package com.aepl.sam.utils;

public class Constants {

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    // Base URL
    public static final String BASE_URL = "http://sampark-qa.accoladeelectronics.com";

    // General URLs
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String EXP_FRGT_PWD_URL = BASE_URL + "/login";
    public static final String DASH_URL = BASE_URL + "/device-dashboard-page";
    public static final String GOV_LINK = BASE_URL + "/govt-servers";
    public static final String DEVICE_LINK = BASE_URL + "/model";
    public static final String ADD_MODEL_LINK = BASE_URL + "/model-firmware";
    public static final String USR_MAN = BASE_URL + "/user-tab";
    public static final String USR_PROFILE = BASE_URL + "/profile";
    public static final String ROLE_MANAGEMENT = BASE_URL + "/user-role";
    public static final String PROD_DEVICE_LINK = BASE_URL + "/production-device-page";
    public static final String DISP_DEVICE_LINK = BASE_URL + "/dispatch-device-page";
    public static final String CREATE_DIS_DEVICE_LINK = BASE_URL + "/dispatch-device-add-page";
    public static final String ROLE_GROUP = BASE_URL + "/role-group";
    public static final String OTA_LINK = BASE_URL + "/ota-batch-page";
    public static final String SIM_MANUAL_UPLOAD = BASE_URL + "/sensorise-sim-manual-upload";

    // Common UI Expectations
    public static final String EXP_LOGO_DISPLAYED = "Logo Displayed";
    public static final String EXP_PAGE_TITLE_TEXT = "AEPL Sampark Diagnostic Cloud";
    public static final String EXP_VALIDATE_BUTTONS_TEXT = "All buttons are displayed and enabled successfully.";
    public static final String EXP_VALIDATE_COMPONENTS_TEXT = "All components are displayed and validated successfully.";
    public static final String EXP_PAGINATION_TEXT = "Pagination works correctly";

    // Footer
    public static final String EXP_VERSION_TEXT = "Version: 1.6.0";
    public static final String EXP_COPYRIGHT_TEXT = "Accolade Electronics Pvt. Ltd.";

    // Login Credentials
    public static final String CUR_PASS = "KZTVvTtE";
    public static final String NEW_PASS = "KZTVvTtE";

    // Validation Error Messages
    public static final String email_error_msg_01 = "This field is required and can't be only spaces.";
    public static final String email_error_msg_02 = "Please enter a valid Email ID.";
    public static final String password_error_msg_01 = "Please Enter Password";
    public static final String password_error_msg_02 = "Minimum 6 characters required.";

    // Toast messages
    public static final String toast_error_msg = "Invalid credentials!!";
    public static final String toast_error_msg_01 = "User Not Found";
    public static final String toast_error_msg_02 = "login Failed due to Incorrect email or password";
    public static final String toast_error_msg_03 = "Validation Error";

    // Test Device Data
    public static final String IMEI = "867950076683091";
    public static final String ICCID = "89916440844825969900";
    public static final String UIN = "ACON4SA162426683091";
    public static final String VIN = "ACCDEV12242083091";

    // Generic Test Data
    public static final String DEVICE_MODEL = "Testing";
    public static final String USER = "suraj";
    public static final String MOBILE_NUMBER = "7385862781";
    public static final String ALT_MOBILE_NUMBER = "9730922327";
    public static final String ISP_1 = "BSNL";
    public static final String ISP_2 = "Airtel";
    public static final String FIRMWARE = "1.2.3";
    public static final String UP_FIRMWARE = "2.0.0";
    public static final String COUNTRY = "India";
    public static final String STATE = "Maharashtra";
    public static final String STATE_ABR = "MH";
    public static final String EMAIL = "surajbhalerao2024@gmail.com";
    public static final String ALT_EMAIL = "surajbhalerao2024@gmail.com";
    public static final String ROLE_TYPE = "PAE MANAGER";
    public static final String QA_MAN = "Shital Shingare";
    public static final String SOFT_MAN = "Abhijeet Jawale";

    // Device info
    public static final String DEVICE_UID = "ACON4SA212240006474";
    public static final String PART_NO_ADD = "PART001";
    public static final String PART_NO_UPDATE = "PART002";
    public static final String CUSTOMER_ADD = "AEPL";
    public static final String CUSTOMER_UPDATE = "TML";

    // Server info
    public static final String GOV_SERVER_NAME = "SURAJ";
    public static final String GOV_STATE_CODE = "SA";

    // File Upload Data
    public static final String BIN_FILE_PATH = "D:\\AEPL_AUTOMATION\\SAM_AUTO\\src\\test\\resources\\SampleUpload\\TCP01.bin";
    public static final String EXPECTED_FILE_NAME = "TCP01.bin";
}
