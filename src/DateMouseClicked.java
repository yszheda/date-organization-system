import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class DateMouseClicked extends MouseAdapter
{
	private DOSView mainView;
	private Date date;
	private GregorianCalendar time;
	public DateMouseClicked(DOSView mainView, Date date, GregorianCalendar time)
	{
		this.mainView = mainView;
		this.date = date;
		this.time = time;
	}
	public void mouseClicked(MouseEvent event) {
		if(event.getClickCount() == 2)
		{
			DateDialog dateDialog = new DateDialog(mainView, date); 
			dateDialog.setVisible(true);
		}
		else if(SwingUtilities.isRightMouseButton(event))
		{
			JTextPane selected = (JTextPane) event.getSource();
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem editMenuItem = new JMenuItem("edit");
			editMenuItem.addActionListener(new editListener(mainView, date));
			popupMenu.add(editMenuItem);
			if(date.containsOnlySingleDay())
			{
				JMenuItem removeMenuItem = new JMenuItem("remove");
				removeMenuItem.addActionListener(new removeListener(mainView,date));
				popupMenu.add(removeMenuItem);
			}
			else
			{
				JMenuItem removeAllDatesMenuItem = new JMenuItem("remove all the dates");
				removeAllDatesMenuItem.addActionListener(new removeListener(mainView,date));
				popupMenu.add(removeAllDatesMenuItem);
				
				JMenuItem removeCurrentDateMenuItem = new JMenuItem("remove the current date");
				removeCurrentDateMenuItem.addActionListener(new removeCurrentDateListener(mainView,date,time));
				popupMenu.add(removeCurrentDateMenuItem);
			}
			popupMenu.show(selected, event.getX(), event.getY());
			
		}
	}
}
class editListener implements ActionListener
{
	private DOSView mainView;
	private Date editDate;
	public editListener(DOSView mainView, Date editDate)
	{
		this.mainView = mainView;
		this.editDate = editDate;
	}
	public void actionPerformed(ActionEvent event) {
		DateDialog dateDialog = new DateDialog(mainView, editDate); 
		dateDialog.setVisible(true);
	}
	
}

class removeListener implements ActionListener
{
	private DOSView mainView;
	private Date removeDate;
	public removeListener(DOSView mainView, Date removeDate)
	{
		this.mainView = mainView;
		this.removeDate = removeDate;
	}
	public void actionPerformed(ActionEvent event) {
		int selection = JOptionPane.showConfirmDialog(null, "Are you sure to remove?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(selection == JOptionPane.OK_OPTION)
		{
			mainView.removeDate(removeDate);
			mainView.save();
			mainView.updateView();
		}
	}
	
}

class removeCurrentDateListener implements ActionListener
{
	private DOSView mainView;
	private Date removeDate;
	private GregorianCalendar time;
	public removeCurrentDateListener(DOSView mainView, Date removeDate, GregorianCalendar time)
	{
		this.mainView = mainView;
		this.removeDate = removeDate;
		this.time = time;
	}
	public void actionPerformed(ActionEvent event) {
		int selection = JOptionPane.showConfirmDialog(null, "Are you sure to remove?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(selection == JOptionPane.OK_OPTION)
		{
			Date afterDate = new Date(removeDate);
			Date beforeDate = new Date(removeDate);
			GregorianCalendar beforeDateEndTime = (GregorianCalendar) time.clone();
			beforeDateEndTime.add(Calendar.DAY_OF_MONTH, -1);
			beforeDate.setEndDate(beforeDateEndTime);
			removeDate.setEndDate(beforeDateEndTime);
			GregorianCalendar afterDateBeginTime = (GregorianCalendar) time.clone();
			afterDateBeginTime.add(Calendar.DAY_OF_MONTH, 1);
			afterDate.setBeginDate(afterDateBeginTime);
			mainView.addDate(beforeDate);
			mainView.addDate(afterDate);
			mainView.removeDate(removeDate);
			mainView.save();
			mainView.updateView();
		}
	}
	
}
