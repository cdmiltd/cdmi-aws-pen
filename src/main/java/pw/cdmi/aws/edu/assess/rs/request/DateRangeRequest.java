package pw.cdmi.aws.edu.assess.rs.request;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public enum DateRangeRequest {
    WEEK,
    MONTH;

    public String getWeeKStartDate(){
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = simpleDateFormat.format(currentDate.getTime());
        return startTime;
    }

    public String getWeeKEndDate(){
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = simpleDateFormat.format(currentDate.getTime());
        return endTime;
    }

}
