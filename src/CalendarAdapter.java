import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarAdapter implements CalenderAdapterInterface {
	private GregorianCalendar calendar;

	public CalendarAdapter() {
		calendar = new GregorianCalendar();
	}

	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}

}
