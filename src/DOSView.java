import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class DOSView {
	private JFrame frame;
	private JPanel contentPane;
	private JPanel viewSwitcher;
	private JPanel dateViewPanel;
	private JButton dayButton;
	private JButton weekButton;
	private JButton monthButton;
	private String viewChoice;
	private MonthView monthView;
	private WeekView weekView;
	private DayView dayView;
	
	private DateSet dateSet;
	
	public DOSView() {
		dateSet = new DateSet();
//		readIn();
		makeFrame();
//		save();
	}
	private void makeFrame() {
		frame = new JFrame("Date Organizer System");
		contentPane = (JPanel) frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
		readIn();		
		// default: show the month view
		viewChoice = "monthView";
		monthView = new MonthView(this, this.dateSet);
		dateViewPanel = new JPanel();
		dateViewPanel.setLayout(new BorderLayout());
		dateViewPanel.add(monthView);
		contentPane.add(dateViewPanel);
		
		makeViewSwitcher();
		
		contentPane.add(viewSwitcher,BorderLayout.NORTH);
		
		frame.pack();
		// place the frame at the center of the screen and show
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width / 2 - frame.getWidth() / 2, d.height / 2
				- frame.getHeight() / 2);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
//		frame.pack();
		save();
	}
	public String getViewChoice() {
		return viewChoice;
	}
	public void setViewChoice(String viewChoice) {
		this.viewChoice = viewChoice;
	}
	private void makeViewSwitcher()
	{
		viewSwitcher = new JPanel();
		viewSwitcher.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 3));

		dayButton = new JButton(" day ");
		dayButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		dayButton.setBackground(Color.PINK);
		dayButton.addActionListener(new DayButtonPressed(this, this.dateSet));
		viewSwitcher.add(dayButton);

		weekButton = new JButton(" week");
		weekButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		weekButton.setBackground(Color.MAGENTA);
		weekButton.addActionListener(new weekButtonPressed(this, this.dateSet));
		viewSwitcher.add(weekButton);

		monthButton = new JButton("month");
		monthButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		monthButton.setBackground(Color.RED);
		monthButton.addActionListener(new MonthButtonPressed(this,this.dateSet));
		viewSwitcher.add(monthButton);
	}
	class DayButtonPressed implements ActionListener
	{
		private DOSView mainView;
		private DateSet dateSet;
		DayButtonPressed(DOSView mainView, DateSet dateSet)
		{
			this.mainView = mainView;
			mainView.readIn();
			this.dateSet = dateSet;
		}
		public void actionPerformed(ActionEvent event) {
			mainView.setViewChoice("dayView");
			mainView.dateViewPanel.removeAll();
			mainView.readIn();
			if(dayView == null)
				dayView = new DayView(mainView, dateSet);
			mainView.dateViewPanel.add(dayView);
			updateView();
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(d.width / 2 - frame.getWidth() / 2, 0);
			mainView.frame.pack();
			mainView.save();
		}
		
	}
	class weekButtonPressed implements ActionListener
	{
		private DOSView mainView;
		private DateSet dateSet;
		weekButtonPressed(DOSView mainView, DateSet dateSet)
		{
			this.mainView = mainView;
			this.dateSet = dateSet;
		}
		public void actionPerformed(ActionEvent event) {
			mainView.setViewChoice("weekView");
			mainView.dateViewPanel.removeAll();
			mainView.readIn();
			if(weekView == null)
				weekView = new WeekView(mainView, dateSet);
			mainView.dateViewPanel.add(weekView);
			updateView();
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(d.width / 2 - frame.getWidth() *2/3 , 0);
			mainView.frame.pack();
			mainView.save();
		}
		
	}
	class MonthButtonPressed implements ActionListener
	{
		private DOSView mainView;
		MonthButtonPressed(DOSView mainView, DateSet dateSet)
		{
			this.mainView = mainView;
		}
		public void actionPerformed(ActionEvent event) {
			mainView.setViewChoice("monthView");
			mainView.dateViewPanel.removeAll();
			mainView.readIn();
			MonthView test = mainView.monthView;
			mainView.dateViewPanel.add(test);
			updateView();
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setLocation(d.width / 2 - frame.getWidth() / 2, d.height / 2
					- frame.getHeight() / 2);
			mainView.frame.pack();
			mainView.save();
		}
		
	}
	public void updateView()
	{
		readIn();
		if(viewChoice.equals("monthView"))
			monthView.updateMonthView();
		if(viewChoice.equals("weekView"))
			weekView.updateWeekView();
		if(viewChoice.equals("dayView"))
			dayView.updateDayView();
	}
	public void readIn() {
		dateSet.readIn();
	}
	public void save() {
		dateSet.save();
	}
	
	public void addDate(Date newDate)
	{
		dateSet.add(newDate);
//		save();
	}
	public void removeDate(Date removeDate)
	{
		dateSet.remove(removeDate);
//		save();
	}
	public ArrayList<Date> getDates()
	{
		return dateSet.getDates();
	}
	private void setDefaultFont()
	{
		Font font = new Font("SansSerif",Font.PLAIN,15);
		UIManager.put("ToolTip.font",font);
		UIManager.put("Table.font",font);
		UIManager.put("TableHeader.font",font);
		UIManager.put("TextField.font",font);
		UIManager.put("ComboBox.font",font);
		UIManager.put("TextField.font",font);
		UIManager.put("PasswordField.font",font);
		UIManager.put("TextArea.font",font);
		UIManager.put("TextPane.font",font);
		UIManager.put("EditorPane.font",font);
		UIManager.put("FormattedTextField.font",font);
		UIManager.put("Button.font",font);
		UIManager.put("CheckBox.font",font);
		UIManager.put("RadioButton.font",font);
		UIManager.put("ToggleButton.font",font);
		UIManager.put("ProgressBar.font",font);
		UIManager.put("DesktopIcon.font",font);
		UIManager.put("TitledBorder.font",font);
		UIManager.put("Label.font",font);
		UIManager.put("List.font",font);
		UIManager.put("TabbedPane.font",font);
		UIManager.put("MenuBar.font",font);
		UIManager.put("Menu.font",font);
		UIManager.put("MenuItem.font",font);
		UIManager.put("PopupMenu.font",font);
		UIManager.put("CheckBoxMenuItem.font",font);
		UIManager.put("RadioButtonMenuItem.font",font);
		UIManager.put("Spinner.font",font);
		UIManager.put("Tree.font",font);
		UIManager.put("ToolBar.font",font);
		UIManager.put("OptionPane.messageFont",font);
		UIManager.put("OptionPane.buttonFont",font); 
	}
	public static void main(String[] args) {
		DOSView test = new DOSView();
		test.setDefaultFont();	
	}
	
	
}
