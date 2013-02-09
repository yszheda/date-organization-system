import java.awt.Color;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Date implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private GregorianCalendar beginDate;
	private GregorianCalendar endDate;
	//period
	private GregorianCalendar fromTime;
	private GregorianCalendar toTime;
	//period
	private Color dateColor;
	private String repeat;
	private int repeatTimeLimit;
	private String location;
	private String comment;
	
	public Date(){
	}
	public Date(String title, GregorianCalendar beginTime, GregorianCalendar endTime, String location, String comment)
	{
		this.title = title;
		this.beginDate = beginTime;
		this.endDate = endTime;
		this.location = location;
		this.comment = comment;
	}
	public Date(String title, GregorianCalendar beginDate,
			GregorianCalendar endDate, GregorianCalendar fromTime,
			GregorianCalendar toTime, Color dateColor, String repeat,
			int repeatTimeLimit, String location, String comment) {
		super();
		this.title = title;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.dateColor = dateColor;
		this.repeat = repeat;
		this.repeatTimeLimit = repeatTimeLimit;
		this.location = location;
		this.comment = comment;
	}
	public Date(Date newDate)
	{
		this.title = newDate.title;
		this.beginDate = newDate.beginDate;
		this.endDate = newDate.endDate;
		this.fromTime = newDate.fromTime;
		this.toTime = newDate.toTime;
		this.dateColor = newDate.dateColor;
		this.repeat = newDate.repeat;
		this.repeatTimeLimit = newDate.repeatTimeLimit;
		this.location = newDate.location;
		this.comment = newDate.comment;
	}
	public int getRepeatTimeLimit() {
		return repeatTimeLimit;
	}
	public void setRepeatTimeLimit(int repeatTimeLimit) {
		this.repeatTimeLimit = repeatTimeLimit;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public Color getDateColor() {
		return dateColor;
	}
	public void setDateColor(Color dateColor) {
		this.dateColor = dateColor;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public GregorianCalendar getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(GregorianCalendar beginDate) {
		this.beginDate = beginDate;
	}
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	public GregorianCalendar getFromTime() {
		return fromTime;
	}
	public void setFromTime(GregorianCalendar fromTime) {
		this.fromTime = fromTime;
	}
	public GregorianCalendar getToTime() {
		return toTime;
	}
	public void setToTime(GregorianCalendar toTime) {
		this.toTime = toTime;
	}
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String toString()
	{ 
		return title;		
	}
//	public boolean equalsDate(Date date)
//	{
//		boolean equals = false;
//		if(title.equals(date.getTitle()) && beginDate.equals(date.getBeginDate()) && endDate.equals(date.getEndDate())
//				&& fromTime.equals(date.getFromTime()) && toTime.equals(date.getToTime())
//				&& location.equals(date.getLocation()) && comment.equals(date.comment))
//			equals = true;
//		return equals;
//	}
	public boolean containsOnlySingleDay()
	{
		boolean flag = false;
		if(repeat.equals("Does not repeat") && equalsBeginDay(endDate))
		{
			flag = true;
		}
		return flag;
	}
	private boolean equalsBeginDay(GregorianCalendar time)
	{
		boolean equals = false;
		int year = time.get(Calendar.YEAR);
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int beginYear = beginDate.get(Calendar.YEAR);
		int beginMonth = beginDate.get(Calendar.MONTH);
		int beginDay = beginDate.get(Calendar.DAY_OF_MONTH);
		if(year == beginYear && month == beginMonth && day == beginDay)
			equals = true;
		return equals;
	}
	public boolean containsDay(GregorianCalendar time)
	{
		boolean contains = false;
		GregorianCalendar checktime = new GregorianCalendar();
		checktime.set(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH));
		if(repeat.equals("Does not repeat"))
		{
			contains = checkDay(checktime);
		}
		if(repeat.equals("Weekly"))
		{
			for(int i = 0; i < repeatTimeLimit; i++)
			{
				if(checkDay(checktime) == true)
				{
					contains = true;
				}
				checktime.add(Calendar.WEEK_OF_YEAR, -1);
			}
//			System.out.println(contains);
		}
		if(repeat.equals("Monthly"))
		{
			for(int i = 0; i < repeatTimeLimit; i++)
			{
				if(checkDay(checktime) == true)
				{
					contains = true;
				}
				checktime.add(Calendar.MONTH, -1);
			}
//			System.out.println(contains);
		}
		if(repeat.equals("Yearly"))
		{
			for(int i = 0; i < repeatTimeLimit; i++)
			{
				if(checkDay(checktime) == true)
				{
					contains = true;
				}
				checktime.add(Calendar.YEAR, -1);
			}
		}
		return contains;
	}
	private boolean checkDay(GregorianCalendar time)
	{
		boolean contains = false;
		boolean beginFlag = false;
		boolean endFlag = false;
		int year = time.get(Calendar.YEAR);
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int beginYear = beginDate.get(Calendar.YEAR);
		int beginMonth = beginDate.get(Calendar.MONTH);
		int beginDay = beginDate.get(Calendar.DAY_OF_MONTH);
		int endYear = endDate.get(Calendar.YEAR);
		int endMonth = endDate.get(Calendar.MONTH);
		int endDay = endDate.get(Calendar.DAY_OF_MONTH);
		if(beginYear<year)
		{
			beginFlag = true;
		}
		else if(beginYear==year)
		{
			if(beginMonth<month)
			{
				beginFlag = true;
			}
			else if(beginMonth==month)
			{
				if(beginDay<=day)
				{
					beginFlag = true;
				}
			}
		}
		if(year<endYear)
		{
			endFlag = true;
		}
		else if(year==endYear)
		{
			if(month<endMonth)
			{
				endFlag = true;
			}
			else if(month==endMonth)
			{
				if(day<=endDay)
				{
					endFlag = true;
				}
			}
		}
		contains = beginFlag && endFlag;
		if(beginFlag == true && endFlag == true)
			contains = true;
		return contains;
//		return ( (time.after(beginDate) || time.equals(beginDate)) && (time.before(endDate) || time.equals(endDate)) );
	}

}
