import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
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



public class WeekView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DOSView mainView;
	private DateSet dateSet;
	
	private int dayPanelWidth = 120;
	private int dayPanelHeight = 32;
	private JPanel tableTitlePanel = new JPanel();
	private JPanel dayTablePanel = new JPanel();
	private JLayeredPane dayLayeredPane = new JLayeredPane();
	private JPanel dayPanel[][] = new JPanel[24][8];
	private JLabel week[] = new JLabel[8];
	private SpinnerDateModel spinnerDate = new SpinnerDateModel();
	private GregorianCalendar calendar = new GregorianCalendar();
	String tableTitle[]={"period","Sunday","Monday","Tuesday","Wensday","Thursday","Friday","Saturday"};
	public WeekView(DOSView mainView, DateSet dateSet)
	{
		this.mainView = mainView;
		this.dateSet = dateSet;
		mainView.readIn();
		
		setLayout(new BorderLayout());
		makeTableTitle();
		makeDayTable();
		showDate();
		makeTimeChooser();
		
		dayLayeredPane.setPreferredSize(new Dimension(dayPanelWidth*8,dayPanelHeight*24));
		JScrollPane dayScrollPane = new JScrollPane();
		dayScrollPane.setViewportView(dayLayeredPane);
		dayScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		dayScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(dayScrollPane,BorderLayout.CENTER);
		
		mainView.save();
	}
	private void makeTableTitle()
	{
		tableTitlePanel.setLayout(new GridLayout(1,8,1,1));
		for(int i=0;i<8;i++){
			week[i]=new JLabel();
			week[i].setText(tableTitle[i]);
			week[i].setBorder(BorderFactory.createRaisedBevelBorder());
			week[i].setHorizontalAlignment(JLabel.CENTER);
			week[i].setFont(new Font("SansSerif", Font.BOLD, 18));
			week[i].setBackground(Color.ORANGE);
			week[i].setForeground(Color.BLACK);
			tableTitlePanel.add(week[i]);
		}
		add(tableTitlePanel,BorderLayout.NORTH);
	}
	private void makeDayTable()
	{
		dayTablePanel.setLayout(new GridLayout(24,8,1,1));
		for(int i = 0; i < 24; i++)
		{
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
			for(int j = 0; j < 8; j++)
			{		
				dayPanel[i][j] = new JPanel();
				if(j != 0)
					dayPanel[i][j].setBackground(Color.WHITE);
				else
					dayPanel[i][j].add(hourPeriodLabel);
				dayPanel[i][j].setSize(new Dimension(dayPanelWidth,dayPanelHeight));
				int currentYear = calendar.get(Calendar.YEAR);
				int currentMonth = calendar.get(Calendar.MONTH);
				int currentDay = calendar.get(Calendar.DAY_OF_MONTH) + j;
				dayPanel[i][j].addMouseListener(new HourPeriodMouseClicked(mainView, currentYear, currentMonth, currentDay, i, (i+1)));
				dayTablePanel.add(dayPanel[i][j]);
			}
		}
		dayTablePanel.setSize(new Dimension(dayPanelWidth*8,dayPanelHeight*24));
		dayLayeredPane.add(dayTablePanel,new Integer(1));
		
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
		spinnerDate.setCalendarField(Calendar.WEEK_OF_MONTH);
		JSpinner spinner = new JSpinner(spinnerDate);
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, " 'Week 'W' in 'M, yyyy  ");
		dateEditor.getTextField().setFont(new Font("SansSerif", Font.BOLD, 16));
		spinner.setEditor(dateEditor);
		spinnerDate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				calendar.setTime( ((SpinnerDateModel)event.getSource()).getDate() );
				calendar.add(Calendar.DAY_OF_WEEK, 1);
				while(calendar.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY)
				{
					calendar.add(Calendar.DAY_OF_WEEK, -1);
				}
				updateWeekView();
			}
		});
		
		timeChooser.add(spinner);
		add(timeChooser,BorderLayout.SOUTH);
	}
	public void updateWeekView()
	{
		dayLayeredPane.removeAll();
		dayTablePanel.removeAll();
		makeDayTable();
		showDate();
		dayLayeredPane.setPreferredSize(new Dimension(dayPanelWidth*8,dayPanelHeight*24));
		dayLayeredPane.repaint();
	}
	private void showDate()
	{
		while(calendar.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY)
		{
			calendar.add(Calendar.DAY_OF_WEEK, -1);
		}
		ArrayList<Date> dateArray = dateSet.getDates();
		for(int i = 0; i < 7; i++)
		{
			int dateOfDayNum = dateSet.countDateOfDay(calendar);
			int dateOfDayCount = 0;
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
					
					int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
					
					int hourPeriodBoxNum;
					if(toHour-fromHour == 0)
						hourPeriodBoxNum = 1;
					else
						hourPeriodBoxNum = toHour-fromHour;
					Point dateTextPaneLocation = dateTextPane.getLocation();
					dateTextPaneLocation.x = dayPanelWidth*weekDay+dateOfDayCount*dayPanelWidth/dateOfDayNum;
					dateTextPaneLocation.y = dayPanelHeight*fromHour;
					dateTextPane.setLocation(dateTextPaneLocation.x ,dayPanelHeight*fromHour);
					dateTextPane.setSize(new Dimension(dayPanelWidth/dateOfDayNum,dayPanelHeight*hourPeriodBoxNum));
					GregorianCalendar currentTime = (GregorianCalendar) calendar.clone();
					dateTextPane.addMouseListener(new DateMouseClicked(mainView,date,currentTime));
					dayLayeredPane.add(dateTextPane,new Integer(2+dateOfDayCount));
					dayLayeredPane.repaint();
				}
				dateOfDayCount ++;
			}
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
}
