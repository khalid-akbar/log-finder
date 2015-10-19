package net.therap.logfinder;

/**
 * @author babar
 * @since 10/15/15
 */
public class LogFinderService {

    public enum TimeFormat {
        HOUR, MINUTE, SECOND
    }

    public int getServerTime(String s) {
        String[] spaceSplits = s.split(" ");
        String timespan = spaceSplits[Constants.SERVER_TIME].split("=")[1];
        String time = timespan.substring(0, timespan.length() - 2);

        return Integer.parseInt(time);
    }

    public String getRequestType(String s) {
        String requestType = "POST";
        String[] spaceSplits = s.split(" ");

        if (spaceSplits[Constants.REQUEST_TYPE].equals("G,")) {
            requestType = "GET";
        }

        return requestType;
    }

    public String getHourlyFormattedString(int currentHour) {
        return "##########\tHour : " + currentHour + "\t##########";
    }

    public int getHour(String s) {
        String[] spaceSplits = s.split(" "); //todo: rename var
        String[] timestamp = spaceSplits[Constants.TIME].split(",");
        String[] hourMinSec = timestamp[0].split(":");

        return Integer.parseInt(hourMinSec[0]);
    }

    public String getUserName(String s) {
        String[] spaceSplits = s.split(" ");
        return spaceSplits[Constants.USER].split(":")[1];
    }

    public String timeFormat(long time, TimeFormat format) {
        String formattedTime = null;
        long hour = time / (3600 * 1000);
        time = time % (3600 * 1000);

        long minute = time / (60 * 1000);
        time = time % (60 * 1000);

        long second = time / 1000;
        long millisecond = time % 1000;

        switch (format) {
            case HOUR:
                formattedTime = hour + " h " + minute + " m " + second + " s " + millisecond + " ms ";
                break;
            case MINUTE:
                formattedTime = (minute + hour * 60) + " m " + second + " s " + millisecond + " ms ";
                break;
            case SECOND:
                formattedTime = (second + hour * 3600 + minute * 60) + " s " + millisecond + " ms ";
                break;
            default:
                formattedTime = time + " ms ";
                break;
        }
        return formattedTime;
    }
}
