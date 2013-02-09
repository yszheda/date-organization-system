import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
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


public class MonthView extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DOSView mainView;
	private DateSet dateSet;
	
	private JPanel tableTitlePanel = new JPanel();
	private JPanel dayTablePanel = new JPanel();
	private JPanel dayPanel[] = new JPanel[42];
	private JLabel week[] = new JLabel[7];
	private SpinnerDateModel spinnerDate = new SpinnerDateModel();
	private GregorianCalendar calendar = new GregorianCalendar();
	private int today;
	private int currentMonth;
	private int currentYear;
	String weeks[]={"Sunday","Monday","Tuesday","Wensday","Thursday","Friday","Saturday"};
	public MonthView(DOSView mainView, DateSet dateSet) {
		this.mainView = mainView;
		this.dateSet = dateSet;
		
		mainView.readIn();
		setLayout(new BorderLayout());
		
		today = calendar.get(Calendar.DAY_OF_MONTH);
		currentMonth = calendar.get(Calendar.MONTH);
		currentYear = calendar.get(Calendar.YEAR);
		
		makeTableTitle();
		makeDayTable();
		makeTimeChooser();
		
		mainView.save();
	}
	private void makeTableTitle()
	{
		tableTitlePanel.setLayout(new GridLayout(1,7,1,1));
		for(int i=0;i<7;i++){
			week[i]=new JLabel();
			week[i].setText(weeks[i]);
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
		dayTablePanel.setLayout(new GridLayout(6,7,1,1));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		for(int i = 0; i < 42; i++)
		{		
			dayPanel[i] = new JPanel();
			dayPanel[i].setBackground(Color.WHITE);
			JScrollPane dayScrollPane = new JScrollPane();
			dayScrollPane.setViewportView(dayPanel[i]);
			dayScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			dayScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			dayTablePanel.add(dayScrollPane);
		}	
		setDay(currentYear,currentMonth);
		setToday(today);
		
		add(dayTablePanel,BorderLayout.CENTER);
	}
	private void makeTimeChooser()
	{
		JPanel timeChooser = new JPanel();
		spinnerDate.setCalendarField(Calendar.MONTH);
		JSpinner spinner = new JSpinner(spinnerDate);
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, " M, yyyy  ");
		dateEditor.getTextField().setFont(new Font("SansSerif", Font.BOLD, 16));
		spinner.setEditor(dateEditor);
		spinnerDate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				GregorianCalendar newCalendar = new GregorianCalendar();
				newCalendar.setTime( ((SpinnerDateModel)event.getSource()).getDate() );
				currentMonth = newCalendar.get(Calendar.MONTH);
				currentYear  = newCalendar.get(Calendar.YEAR);
				setDay(currentYear, currentMonth);
			}
		});
		
		timeChooser.add(spinner);
		add(timeChooser,BorderLayout.SOUTH);
	}
	public void updateMonthView()
	{
		setDay(currentYear, currentMonth);
//		dayTablePanel.repaint();
	}
	public void setDay(int year, int month)
	{
		mainView.readIn();
		calendar.set(year, month, 1, 0, 0, 0);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		for(int i = 0; i < 42; i++)
		{
			MouseListener[] mouseListeners = dayPanel[i].getMouseListeners();
			for(MouseListener mouseListener : mouseListeners)
			{
				dayPanel[i].removeMouseListener(mouseListener);
			}
			dayPanel[i].removeAll();
			dayPanel[i].repaint();
			dayPanel[i].setLayout(new BoxLayout(dayPanel[i],BoxLayout.Y_AXIS));
		}		
		int weeknum = 0;
		do{
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			JLabel dayOfMonth = new JLabel(""+day+"\n");
			dayOfMonth.setFont(new Font("SansSerif", Font.BOLD, 18));
			dayOfMonth.setForeground(Color.BLUE);
			dayPanel[ weekday+weeknum*7-1 ].add(dayOfMonth);		
			showDate(dayPanel[ weekday+weeknum*7-1 ], calendar);
			dayPanel[ weekday+weeknum*7-1 ].addMouseListener(new DayMouseClicked(mainView, year, month, weekday+weeknum*7-1));

			dayPanel[ weekday+weeknum*7-1 ].validate();
			dayPanel[ weekday+weeknum*7-1 ].repaint();
			setVisible(true);
			
			if(weekday == Calendar.SATURDAY)
				weeknum++;
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			
			weekday = calendar.get(Calendar.DAY_OF_WEEK);	
		}while(calendar.get(Calendar.MONTH) == month);
	}
	private void setToday(int today)
	{
		dayPanel[ today+1 ].setBackground(Color.GREEN);
	}
	class DayMouseClicked extends MouseAdapter
	{
		private DOSView mainView;
		private int currentYear;
		private int currentDay;
		private int currentMonth;
		public DayMouseClicked(DOSView mainView, int currentYear, int currentMonth, int currentDay)
		{
			this.mainView = mainView;
			this.currentYear = currentYear;
			this.currentMonth = currentMonth;
			this.currentDay = currentDay;
		}
		public void mouseClicked(MouseEvent event) {
			if(event.getClickCount() == 2)
			{
				DateDialog dateDialog = new DateDialog(mainView, currentYear, currentMonth, currentDay); 
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
	public void updateDateSet(DateSet dateSet)
	{
		this.dateSet = dateSet;
	}
	
	private void showDate(JPanel dayPanel, GregorianCalendar time)
	{
		mainView.readIn();
		ArrayList<Date> dateArray = dateSet.getDates();
		for(int i = 0; i < dateArray.size() ; i++)
		{
			if(dateArray.get(i).containsDay(calendar))
			{
				JTextPane dateTextPane = new JTextPane();
				dateTextPane.setText(dateArray.get(i).getTitle());
				dateTextPane.setBackground(dateArray.get(i).getDateColor());
				dateTextPane.setFont(new Font("SansSerif",Font.PLAIN,15));
				dateTextPane.setEditable(false);
				GregorianCalendar currentTime = (GregorianCalendar) time.clone();
				dateTextPane.addMouseListener(new DateMouseClicked(mainView,dateArray.get(i),currentTime));
				dayPanel.add(dateTextPane);
				dayPanel.repaint();
			}		
		}
//		for(Date date : dateArray)
//		{
//			if(date.containsDay(time))
//			{
//				JTextPane dateTextPane = new JTextPane();
//				dateTextPane.setText(date.getTitle());
//				dateTextPane.setBackground(date.getDateColor());
//				dateTextPane.setFont(new Font("SansSerif",Font.PLAIN,15));
//				dateTextPane.setEditable(false);
//				GregorianCalendar currentTime = (GregorianCalendar) time.clone();
//				dateTextPane.addMouseListener(new DateMouseClicked(mainView,date,currentTime));
//				dayPanel.add(dateTextPane);
//				dayPanel.repaint();
//			}		
//		}
	}
}
