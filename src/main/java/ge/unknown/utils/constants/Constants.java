package ge.unknown.utils.constants;

import java.io.File;

/**
 * Created by MJaniko on 10/26/2016.
 */
public class Constants {

    public static final String BASE_URL = "http://212.72.150.51:8080/ims/srv/online.jsp?data=";

    public static final class Products {
        public static final Long BAGGADZE_INS = 260L;//	ბარგის დაზღვევა

        public static final String[] VALID_EXTENSIONS = {"png", "jpg", "jpeg", "doc", "docx", "xls", "xlsx", "pdf"};

        public static final String[] VALID_OBJECT_NAMES = {
                "USER_IMG",
                "SLIDER_IMG",
                "GENERAL",
        };

        /*
        * 5000$ - წლიური 50$
        * 10000 - წლიური 75$
        * 20000 - წლიური 95$
        * 30000 - წლიური 115$
        * */
        public static final Long TPL1 = 27L;//მე-3 პირთა წინაშე პასუხისმგებლობა (ნებაყოფლობითი)(TPL)
        public static final Long TPL2 = 28L;//მე-3 პირთა წინაშე პასუხისმგებლობა (ნებაყოფლობითი)(TPL)

        /*
         * 5000$ - წლიური 15$
         * 10000 - წლიური 25$
         * 20000 - წლიური 35$
         * */
        public static final Long MPA1 = 14L;//მძღოლისა და მგზავრის უბედური შემთხვევისაგან დაზღვევა (MPA)
        public static final Long MPA2 = 15L;//მძღოლისა და მგზავრის უბედური შემთხვევისაგან დაზღვევა (MPA)




        public static final Long TRAVEL_INS = 10L;//სამოგზაურო დაზღვევა (ინდივიდუალური)
        public static final Long AUTO_INS = 22L;//სახმელეთო ავტოსატრანსპორტო საშუალებათა დაზღვევა (ინდივიდუალური)
        public static final Long TRANSPORTER_INS = 32L;//სახმელეთო გადამზიდველის პასუხისმგებლობის დაზღვევა (ტირ კარნეტი)
    }

    public static final class UploadHelpers {
        private static final String HOME = String.format("%s%s", System.getProperty("user.home"), File.separator);
        public static final String UPLOADS = String.format("%s%s%s", HOME, "uploads", File.separator);
        public static final String USER_IMG = String.format("%s%s", UPLOADS, "user");
    }

    public static final class ErrorCodes {
        public class ErrorResponse {
            public static final String ACCESS_IS_DENIED = "access_is_denied";
            public static final String UNKNOWN = "unknown";
            public static final String DUPLICATE_RECORD = "DUPLICATE_RECORD";
            public static final String RECORD_IS_USED_IN_OTHER_TABLES = "RECORD_IS_USED_IN_OTHER_TABLES";
            public static final String PERSISTENCE_EXCEPTION = "javax.persistence.PersistenceException";
        }
    }

    public enum SmsMessage {
        NONE(""),
        PERSON_REMINDER_MSG("Bike N:%s\nPin:%s\nStart at:%s\nFinish At:%s\nTimeLeft:%s\nCall Center 442222"),
        BIKE_TAKEN("Bike N:%s\nPin:%s\nStart at:%s\nFinish At:%s\nCall Center 442222"),
        THANK_YOU("Thank you!"),
        BIKE_PSW_RESET("Bike N:%s\nNew Pin:%s"),
        BANK_PERSON_BIKE_ARRIVED("Bike N:%s Arrived!");

        private String txt;

        SmsMessage(String txt) {
            this.txt = txt;
        }

        public String getTxt() {
            return this.txt;
        }
    }

    public enum SuccessResponse {
        NONE(0),
        BIKE_TAKEN(2001),
        BIKE_RECEIVED(2002);

        private int code;

        SuccessResponse(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum ErrorResponse {
        NONE(0),
        PARAM_ERROR(1001),
        NO_SUCH_CARD(1002),
        NO_SUCH_BIKE(1003),
        NO_SUCH_STATION(1004),
        NO_BIKE_ON_STATION(1005),
        PERSON_NOT_ALLOWED_BLACKED(1006),
        PERSON_IS_DEACTIVATED(1007),
        CARD_IS_BLOCKED(1008),
        CARD_HOLDER_IS_EMPTY(1009),
        YOU_HAVE_ALREADY_BIKE(1010),
        SOMEONE_HAS_THIS_BIKE(1011),
        NO_PATH_RECORD_FOR_BIKE(1012),
        WRONG_BIKE_OWNERSHIP(1013),
        BIKE_IS_ALREADY_RETURNED(1014),
        BAK_DOES_NOT_HAVE_A_STATION(1015),
        CANT_TAKE_BIKE(1016);

        private int errCode;

        ErrorResponse(int code) {
            this.errCode = code;
        }

        public int getErrCode() {
            return errCode;
        }
    }

    public static final class ControllerCodes {
        public static final String STRING_EMPTY = "";
        public static final String PAGE_NUMBER = "pageNumber";
        public static final String PAGE_NUMBER_DEFAULT_VALUE = "0";
        public static final String PAGE_SIZE_DEFAULT_VALUE = "10";
        public static final String IS_ASCENDING_DEFAULT_VALUE = "true";
        public static final String DEFAULT_SORT_BY = "id";

        public static final String SLASH = "/";
        public static final String LIST = "list";
        public static final String LAYOUT = "layout";
        public static final String PUT = "put";
        public static final String DELETE = "delete";
        public static final String UPDATE = "update";
        public static final String KEY = "key";
        public static final String VALUE = "value";
    }
}
