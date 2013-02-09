import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DOSView mainView;
	private DateSet dateSet;

	private Box hourTableBox = new Box(BoxLayout.Y_AXIS);
	private JLayeredPane hourTableLayeredPane = new JLayeredPane();
	private Box hourPeriodBox[] = new Box[24];
	private JPanel hourPeriodPanel[] = new JPanel[24];
	private int hourPeriodBoxWidth = 600;
	private int hourPeriodBoxHeight = 27;
	
	private SpinnerDateModel spinnerDate = new SpinnerDateModel();
	private GregorianCalendar calendar = new GregorianCalendar();
	public DayView(DOSView mainView, DateSet dateSet)
	{
		this.mainView = mainView;
		this.dateSet = dateSet;
		mainView.readIn();
		setLayout(new BorderLayout());
		makeHourPeriodTable();
		makeTimeChooser();
		
		showDate();
		
		JScrollPane hourPeriodScrollPane = new JScrollPane();
		hourPeriodScrollPane.setViewportView(hourTableLayeredPane);
		hourPeriodScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hourPeriodScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(hourPeriodScrollPane,BorderLayout.CENTER);
		
		mainView.save();
	}
	public void updateDayView()
	{
		hourTableLayeredPane.removeAll();
		hourTableLayeredPane.repaint();
		makeHourPeriodTable();
		showDate();
		hourTableLayeredPane.repaint();
	}
	private void makeHourPeriodTable()
	{
		for(int i = 0; i < 24; i++)
		{
			hourPeriodBox[i] = Box.createHorizontalBox();
			String hourPeriodString;
			String beginHourString = ""+i+":00";
			String endHourString = ""+(i+1)+":00";
			if(i<10)
				beginHourString = "0"+beginHourString;
			if(i<9)
				endHourString = "0"+endHourString;
			hourPeriodString = beginHourString+" ~ "+endHourString;
			JLabel hourPeriodLabel = new JLabel(hourPeriodString);
			hourPeriodLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
			hourPeriodLabel.setBorder( BorderFactory.createEtchedBorder() );
			hourPeriodBox[i].add(hourPeriodLabel);
			hourPeriodPanel[i] = new JPanel();
			hourPeriodPanel[i].setBackground(Color.WHITE);
			hourPeriodPanel[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
			int currentYear = calendar.get(Calendar.YEAR);
			int currentMonth = calendar.get(Calendar.MONTH);
			int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
			hourPeriodPanel[i].addMouseListener(new HourPeriodMouseClicked(mainView, currentYear, currentMonth, currentDay, i, (i+1)));
			hourPeriodBox[i].add(hourPeriodPanel[i]);
			hourPeriodBox[i].setSize(new Dimension(hourPeriodBoxWidth,hourPeriodBoxHeight));
			hourTableBox.add(hourPeriodBox[i]);
		}
		hourTableBox.setSize(new Dimension(hourPeriodBoxWidth,hourPeriodBoxHeight*24));

		hourTableLayeredPane.add(hourTableBox,new Integer(1));
		hourTableLayeredPane.setPreferredSize(new Dimension(hourPeriodBoxWidth,hourPeriodBoxHeight*24+1));	
	}
	class HourPeriodMouseClicked extends MouseAdapter
	{
		private DOSView mainView;
		private int currentYear;
		private int currentDay;
		private int currentMonth;
		private int fromHour;
		private int toHour;
		public HourPeriodMouseClicked(DOSView mainView, int currentYear, int currentMonth, int currentDay, int fromHour, int toHour)
		{
			this.mainView = mainView;
			this.currentYear = currentYear;
			this.currentMonth = currentMonth;
			this.currentDay = currentDay;
			this.fromHour = fromHour;
			this.toHour = toHour;
		}
		public void mouseClicked(MouseEvent event) {
			if(event.getClickCount() == 2)
			{
				DateDialog dateDialog = new DateDialog(mainView, currentYear, currentMonth, currentDay, fromHour, toHour); 
				dateDialog.setVisible(true);
			}
			else if(SwingUtilities.isRightMouseButton(event))
			{
				JPanel selected = (JPanel) event.getSource();
				JPopupMenu popupMenu = new JPopupMenu();
				JMenuItem newMenuItem = new JMenuItem("new");
				newMenuItem.addActionListener(new newListener(mainView, currentYear, currentMonth, currentDay));
				popupMenu.add(newMenuItem);
				popupMenu.show(selected, event.getX(), event.getY());
				
			}
		}
	}
	class newListener implements ActionListener
	{
		private DOSView mainView;
		private int currentYear;
		private int currentDay;
		private int currentMonth;
		public newListener(DOSView mainView, int currentYear, int currentMonth, int currentDay)
		{
			this.mainView = mainView;
			this.currentYear = currentYear;
			this.currentMonth = currentMonth;
			this.currentDay = currentDay;
		}
		public void actionPerformed(ActionEvent event) {
			DateDialog dateDialog = new DateDialog(mainView, currentYear, currentMonth, currentDay); 
			dateDialog.setVisible(true);
		}
	}
	private void makeTimeChooser()
	{
		JPanel timeChooser = new JPanel();
		spinnerDate.setCalendarField(Calendar.DAY_OF_MONTH);
		JSpinner spinner = new JSpinner(spinnerDate);
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, " d, M, yyyy  ");
		dateEditor.getTextField().setFont(new Font("SansSerif", Font.BOLD, 16));
		spinner.setEditor(dateEditor);
		spinnerDate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				calendar.setTime( ((SpinnerDateModel)event.getSource()).getDate() );
				updateDayView();
			}
		});
		timeChooser.add(spinner);
		add(timeChooser,BorderLayout.SOUTH);
	}
	private void showDate()
	{
		mainView.readIn();
		ArrayList<Date> dateArray = dateSet.getDates();
		int dateOfDayCount = 0;
		int dateOfDayNum = dateSet.countDateOfDay(calendar);
//		for(int i = 0; i < dateArray.size() ; i++)
//		{
//			if(dateArray.get(i).containsDay(calendar))
//			{
//				GregorianCalendar fromTime = dateArray.get(i).getFromTime();
//				GregorianCalendar toTime = dateArray.get(i).getToTime();
//				int fromHour = fromTime.get(Calendar.HOUR_OF_DAY);
//				int toHour = toTime.get(Calendar.HOUR_OF_DAY);
//				
//				
//				JTextPane dateTextPane = new JTextPane();
//				dateTextPane.setText(dateArray.get(i).getTitle());
//				dateTextPane.setBackground(dateArray.get(i).getDateColor());
//				dateTextPane.setFont(new Font("SansSerif",Font.PLAIN,15));
//				dateTextPane.setEditable(false);
//				int hourPeriodBoxNum;
//				if(toHour-fromHour == 0)
//					hourPeriodBoxNum = 1;
//				else
//					hourPeriodBoxNum = toHour-fromHour;
//				
//				Point dateTextPaneLocation = dateTextPane.getLocation();
//				dateTextPaneLocation.x = 110+dateOfDayCount*hourPeriodBoxWidth/dateOfDayNum;
//				dateTextPaneLocation.y = hourPeriodBoxHeight*fromHour;
//				dateTextPane.setLocation(dateTextPaneLocation.x , dateTextPaneLocation.y);
//				dateTextPane.setSize(new Dimension(hourPeriodBoxWidth/dateOfDayNum,hourPeriodBoxHeight*hourPeriodBoxNum));
//				GregorianCalendar currentTime = (GregorianCalendar) calendar.clone();
//				dateTextPane.addMouseListener(new DateMouseClicked(mainView,dateArray.get(i),currentTime));
//				hourTableLayeredPane.add(dateTextPane,new Integer(2+dateOfDayCount));
//				hourTableLayeredPane.repaint();
//				dateOfDayCount ++;
//			}
//		}
		for(Date date : dateArray)
		{
			if(date.containsDay(calendar))
			{
				GregorianCalendar fromTime = date.getFromTime();
				GregorianCalendar toTime = date.getToTime();
				int fromHour = fromTime.get(Calendar.HOUR_OF_DAY);
				int toHour = toTime.get(Calendar.HOUR_OF_DAY);
				
				
				JTextPane dateTextPane = new JTextPane();
				dateTextPane.setText(date.getTitle());
				dateTextPane.setBackground(date.getDateColor());
				dateTextPane.setFont(new Font("SansSerif",Font.PLAIN,15));
				dateTextPane.setEditable(false);
				int hourPeriodBoxNum;
				if(toHour-fromHour == 0)
					hourPeriodBoxNum = 1;
				else
					hourPeriodBoxNum = toHour-fromHour;
				
				Point dateTextPaneLocation = dateTextPane.getLocation();
				dateTextPaneLocation.x = 110+dateOfDayCount*hourPeriodBoxWidth/dateOfDayNum;
				dateTextPaneLocation.y = hourPeriodBoxHeight*fromHour;
				dateTextPane.setLocation(dateTextPaneLocation.x , dateTextPaneLocation.y);
				dateTextPane.setSize(new Dimension(hourPeriodBoxWidth/dateOfDayNum,hourPeriodBoxHeight*hourPeriodBoxNum));
				GregorianCalendar currentTime = (GregorianCalendar) calendar.clone();
				dateTextPane.addMouseListener(new DateMouseClicked(mainView,date,currentTime));
				hourTableLayeredPane.add(dateTextPane,new Integer(2+dateOfDayCount));
				hourTableLayeredPane.repaint();
				dateOfDayCount ++;
			}
		}
	}	
}
