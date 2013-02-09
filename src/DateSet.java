import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DateSet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Date> dates;
	public DateSet()
	{
		dates = new ArrayList<Date>();
	}
	public DateSet(DateSet newDateSet)
	{
		this.dates = newDateSet.dates;
	}
	public void add(Date newDate)
	{
		dates.add(newDate);
	}
	public void remove(Date removeDate)
	{
		dates.remove(removeDate);	
	}
	public ArrayList<Date> getDates()
	{
		return dates;
	}
	public int countDateOfDay(GregorianCalendar time)
	{
		int count = 0;
		for(Date date : dates)
		{
			if(date.containsDay(time))
			{
				count ++;
			}
		}
		return count;
	}
	public void readIn() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					"date.txt"));
			DateSet newDateSet;
			newDateSet = (DateSet) in.readObject();
			this.dates = newDateSet.dates;
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void save() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("date.txt"));
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
