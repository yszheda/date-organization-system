import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class DateDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DOSView mainView;
	private String title;
	private int beginYearNum;
	private int beginMonthNum;
	private int beginDayNum;
	private int endYearNum;
	private int endMonthNum;
	private int endDayNum;
	private int fromHourNum;
	private int fromMinuteNum;
	private int toHourNum;
	private int toMinuteNum;
	private String location;
	private String comment;
	private Color dateColor;
	private String repeat;
	private int repeatTimeLimit;
	String repeatChoice[] = {"Does not repeat","Weekly","Monthly","Yearly"};
	
	private Container contentPane;
	private GridBagConstraints constraint;
	private JTextField titleTextField;
	private JTextField beginYearTextField;
	private JComboBox beginMonthComboBox;
	private JTextField beginDayTextField;
	private JTextField fromHourTextField;
	private JTextField fromMinuteTextField;
	private JTextField endYearTextField;
	private JComboBox endMonthComboBox;
	private JTextField endDayTextField;
	private JTextField toHourTextField;
	private JTextField toMinuteTextField;
	private JComboBox repeatComboBox;
	private JTextField repeatTimeTextField;
	private JTextField locationTextField;
	private JTextArea commentTextField;
	private JPanel dataPanel;
	private JPanel endTimePanel;
	private JPanel periodPanel;
	private JPanel repeatPanel;
	private JPanel buttonPanel;
	
	private Date currentDate;
	private JColorChooser colorChooser;
	public DateDialog(DOSView mainView, int currentYear, int currentMonth, int currentDay) 
	{
		
		this.mainView = mainView;
		this.endYearNum = this.beginYearNum = currentYear;
		this.endMonthNum = this.beginMonthNum = currentMonth;
		this.endDayNum = this.beginDayNum = currentDay-1;
		
		contentPane = getContentPane();
		makeDataPanel();		
		makeNewButtonPanel();
		setSize(500, 360);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight()
				/ 2);

		setUndecorated(true);

		mainView.save();
			
	}
	public DateDialog(DOSView mainView, int currentYear, int currentMonth, int currentDay, int fromHour, int toHour) 
	{
		
		this.mainView = mainView;
		this.endYearNum = this.beginYearNum = currentYear;
		this.endMonthNum = this.beginMonthNum = currentMonth;
		this.endDayNum = this.beginDayNum = currentDay-1;
		this.fromHourNum = fromHour;
		this.toHourNum = toHour;
		
		contentPane = getContentPane();
		makeDataPanel();		
		makeNewButtonPanel();
		setSize(500, 360);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight()
				/ 2);
		
		setUndecorated(true);

		mainView.save();
			
	}
	public DateDialog(DOSView mainView, Date editDate)
	{
		this.mainView = mainView;
		this.title = editDate.getTitle();
		GregorianCalendar beginDate = editDate.getBeginDate();
		this.beginYearNum = beginDate.get(Calendar.YEAR);
		this.beginMonthNum = beginDate.get(Calendar.MONTH);
		this.beginDayNum = beginDate.get(Calendar.DAY_OF_MONTH);
		GregorianCalendar endDate = editDate.getEndDate();
		this.endYearNum = endDate.get(Calendar.YEAR);
		this.endMonthNum = endDate.get(Calendar.MONTH);
		this.endDayNum = endDate.get(Calendar.DAY_OF_MONTH);
		GregorianCalendar fromTime = editDate.getFromTime();
		this.fromHourNum = fromTime.get(Calendar.HOUR_OF_DAY);
		this.fromMinuteNum = fromTime.get(Calendar.MINUTE);
		GregorianCalendar toTime = editDate.getToTime();
		this.toHourNum = toTime.get(Calendar.HOUR_OF_DAY);
		this.toMinuteNum = toTime.get(Calendar.MINUTE);
		this.dateColor = editDate.getDateColor();
		this.repeat = editDate.getRepeat();
		this.repeatTimeLimit = editDate.getRepeatTimeLimit();
		this.location = editDate.getLocation();
		this.comment = editDate.getComment();
		
		this.currentDate = editDate;
		
		contentPane = getContentPane();
		makeDataPanel();		
		makeEditButtonPanel(editDate);
		setSize(500, 380);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight()
				/ 2);

		setUndecorated(true);
		
		mainView.save();
		
	}
	private void makeNewButtonPanel()
	{
		JButton sureButton = new JButton("Sure");
		sureButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		sureButton.addActionListener(new SureListener(this));

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		buttonPanel = new JPanel();
		buttonPanel.add(sureButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setBackground(Color.YELLOW);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	private void makeEditButtonPanel(Date editDate)
	{
		JButton updateButton = new JButton("Update");
		updateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		updateButton.addActionListener(new UpdateListener(this, currentDate));

		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		buttonPanel = new JPanel();
		buttonPanel.add(updateButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setBackground(Color.YELLOW);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	private void makeDataPanel()
	{
		dataPanel = new JPanel();
		dataPanel.setLayout(new GridBagLayout());
		constraint = new GridBagConstraints();
		
		setTitle("Date Information");
		makeTitleItem(0);
		makeBeginTimeItem(1);
		makeEndTimeItem(2);
		makeDuringPeriod(3);
		makeRepeatItem(4);
		makeColorItem(5);
		makeLocationItem(6);
		makeCommentItem(7);
		
		dataPanel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		dataPanel.setBackground(Color.YELLOW);
		contentPane.add(dataPanel);

	}
	private void makeDuringPeriod(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;
		dataPanel.add(new JLabel("from"), constraint);
		periodPanel = new JPanel();
		periodPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fromHourTextField = new JTextField(""+fromHourNum, 3);
		periodPanel.add(fromHourTextField);
		periodPanel.add(new JLabel(" : "));
		fromMinuteTextField = new JTextField(""+fromMinuteNum, 3);
		periodPanel.add(fromMinuteTextField);
		periodPanel.add(new JLabel(" to "));
		toHourTextField = new JTextField(""+toHourNum, 3);
		periodPanel.add(toHourTextField);
		periodPanel.add(new JLabel(" : "));
		toMinuteTextField = new JTextField(""+toMinuteNum, 3);
		periodPanel.add(toMinuteTextField);
		periodPanel.setBackground(Color.YELLOW);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;
		dataPanel.add(periodPanel, constraint);
	}
	private void makeTitleItem(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;
		dataPanel.add(new JLabel("title"), constraint);
		if(title == null)
			titleTextField = new JTextField("", 10);
		else
			titleTextField = new JTextField(""+title, 10);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;
		dataPanel.add(titleTextField, constraint);
	}
	private void makeBeginTimeItem(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;
		dataPanel.add(new JLabel("begining time"), constraint);
		JPanel beginTimePanel = new JPanel();
		beginTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		beginYearTextField = new JTextField(""+beginYearNum, 5);
		beginTimePanel.add(beginYearTextField);
		beginTimePanel.add(new JLabel("年"));
		beginMonthComboBox = new JComboBox();
		beginMonthComboBox.setEditable(false);
		beginMonthComboBox.addItem(beginMonthNum+1);
		for (int i = 0; i < 12; i++) {
			beginMonthComboBox.addItem(i+1);
		}
		
		beginTimePanel.add(beginMonthComboBox);
		beginTimePanel.add(new JLabel("月"));
		beginDayTextField = new JTextField(""+beginDayNum, 3);
		beginTimePanel.add(beginDayTextField);
		beginTimePanel.add(new JLabel("日"));
		beginTimePanel.setBackground(Color.YELLOW);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;
		dataPanel.add(beginTimePanel, constraint);

	}
	private void makeEndTimeItem(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;

		dataPanel.add(new JLabel("ending time"), constraint);
		endTimePanel = new JPanel();
		endTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		endYearTextField = new JTextField(""+endYearNum, 5);
		endTimePanel.add(endYearTextField);
		endTimePanel.add(new JLabel("年"));
		endMonthComboBox = new JComboBox();
		endMonthComboBox.setEditable(false);
		endMonthComboBox.addItem(endMonthNum+1);
		for (int i = 0; i < 12; i++) {
			endMonthComboBox.addItem(i+1);
		}
		endTimePanel.add(endMonthComboBox);
		endTimePanel.add(new JLabel("月"));
		endDayTextField = new JTextField(""+endDayNum, 3);
		endTimePanel.add(endDayTextField);
		endTimePanel.add(new JLabel("日"));
		endTimePanel.setBackground(Color.YELLOW);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;
		dataPanel.add(endTimePanel, constraint);
	}
	private void makeColorItem(int itemLocation)
	{

		
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;

		dataPanel.add(new JLabel("color"), constraint);
		
		colorChooser = new JColorChooser();
		JButton colorChooserButton = new JButton("choose color");
		colorChooserButton.addActionListener(new ChooseColorListener());
		colorChooser.add(colorChooserButton);
		constraint.gridx = 1;
		constraint.gridy = itemLocation;

		dataPanel.add(colorChooserButton, constraint);
	}
	class ChooseColorListener implements ActionListener
	{

		public void actionPerformed(ActionEvent event) {
			Color defaultColor;
			if(currentDate == null)
				defaultColor = Color.red;
			else
				defaultColor = currentDate.getDateColor();
			dateColor = JColorChooser.showDialog((JButton)event.getSource(), "choose color", defaultColor);	
		}
	}
	private void makeRepeatItem(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;
		dataPanel.add(new JLabel("repeats"), constraint);
		repeatPanel = new JPanel();
		repeatPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		repeatComboBox = new JComboBox();
		repeatComboBox.setEditable(false);
		if(repeat != null)
			repeatComboBox.addItem(repeat);
		for(int i = 0; i < 4; i++)
		{
			repeatComboBox.addItem(repeatChoice[i]);
		}
		repeatPanel.add(repeatComboBox);
		repeatPanel.add(new JLabel(" limit of repeat times "));
		repeatTimeTextField = new JTextField(""+repeatTimeLimit,3);
		repeatPanel.add(repeatTimeTextField);
		repeatPanel.setBackground(Color.YELLOW);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;
		dataPanel.add(repeatPanel, constraint);
	}
	private void makeLocationItem(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;

		dataPanel.add(new JLabel("location"), constraint);
		if(location == null)
			locationTextField = new JTextField("", 10);
		else
			locationTextField = new JTextField(""+location, 10);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;

		dataPanel.add(locationTextField, constraint);
	}
	private void makeCommentItem(int itemLocation)
	{
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = itemLocation;

		dataPanel.add(new JLabel("comment"), constraint);
		commentTextField = new JTextArea(3, 30);
		if(comment != null)
			commentTextField.setText(comment);
		JScrollPane scrollPane = new JScrollPane(commentTextField);
		commentTextField.setLineWrap(true);
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 1;
		constraint.gridy = itemLocation;

		dataPanel.add(scrollPane, constraint);
	}

	class SureListener implements ActionListener
	{
		private DateDialog dateDialog;
		public SureListener(DateDialog dateDialog)
		{
			this.dateDialog = dateDialog;
		}
		public void actionPerformed(ActionEvent event) {
			Date newDate = new Date();
			title = titleTextField.getText().trim();
			newDate.setTitle(title);
			
			beginDayNum = Integer.parseInt(beginDayTextField.getText().trim());
			beginMonthNum = (Integer) beginMonthComboBox.getSelectedItem();
			beginYearNum = Integer.parseInt(beginYearTextField.getText().trim());			
			GregorianCalendar beginDate = new GregorianCalendar();
			beginDate.set(beginYearNum, beginMonthNum-1, beginDayNum, 0, 0, 0);			
			newDate.setBeginDate(beginDate);
						
			endDayNum = Integer.parseInt(endDayTextField.getText().trim());
			endMonthNum = (Integer) endMonthComboBox.getSelectedItem();
			endYearNum = Integer.parseInt(endYearTextField.getText().trim());
			GregorianCalendar endDate = new GregorianCalendar();
			endDate.set(endYearNum, endMonthNum-1, endDayNum, 0, 0, 0);
			newDate.setEndDate(endDate);
			
			fromHourNum = Integer.parseInt(fromHourTextField.getText().trim());
			fromMinuteNum = Integer.parseInt(fromMinuteTextField.getText().trim());
			GregorianCalendar fromTime = new GregorianCalendar();
			fromTime.set(endYearNum, endMonthNum-1, endDayNum);
			fromTime.set(Calendar.HOUR_OF_DAY, fromHourNum);
			fromTime.set(Calendar.MINUTE, fromMinuteNum);
			newDate.setFromTime(fromTime);
			
			toHourNum = Integer.parseInt(toHourTextField.getText().trim());
			toMinuteNum = Integer.parseInt(toMinuteTextField.getText().trim());
			GregorianCalendar toTime = new GregorianCalendar();
			toTime.set(Calendar.HOUR_OF_DAY, toHourNum);
			toTime.set(Calendar.MINUTE, toMinuteNum);
			newDate.setToTime(toTime);
			
			newDate.setDateColor(dateColor);
			
			repeat = (String) repeatComboBox.getSelectedItem();
			newDate.setRepeat(repeat);
			repeatTimeLimit = Integer.parseInt(repeatTimeTextField.getText().trim());
			newDate.setRepeatTimeLimit(repeatTimeLimit);
			
			location = locationTextField.getText().trim();
			newDate.setLocation(location);
			
			comment = commentTextField.getText();
			newDate.setComment(comment);
			
			mainView.addDate(newDate);
			mainView.save();
			mainView.updateView();
			mainView.save();
			dateDialog.setVisible(false);
			mainView.save();
		}
		
	}
	
	class UpdateListener implements ActionListener
	{
		private DateDialog dateDialog;
		private Date date;
		public UpdateListener(DateDialog dateDialog, Date date)
		{
			this.dateDialog = dateDialog;
			this.date = date;
		}
		public void actionPerformed(ActionEvent event) {
			title = titleTextField.getText().trim();
			date.setTitle(title);
			
			beginDayNum = Integer.parseInt(beginDayTextField.getText().trim());
			beginMonthNum = (Integer) beginMonthComboBox.getSelectedItem();
			beginYearNum = Integer.parseInt(beginYearTextField.getText().trim());			
			GregorianCalendar beginDate = new GregorianCalendar();
			beginDate.set(beginYearNum, beginMonthNum-1, beginDayNum, 0, 0, 0);
			date.setBeginDate(beginDate);
						
			endDayNum = Integer.parseInt(endDayTextField.getText().trim());
			endMonthNum = (Integer) endMonthComboBox.getSelectedItem();
			endYearNum = Integer.parseInt(endYearTextField.getText().trim());
			GregorianCalendar endDate = new GregorianCalendar();
			endDate.set(endYearNum, endMonthNum-1, endDayNum, 0, 0, 0);
			date.setEndDate(endDate);

			
			fromHourNum = Integer.parseInt(fromHourTextField.getText().trim());
			fromMinuteNum = Integer.parseInt(fromMinuteTextField.getText().trim());
			GregorianCalendar fromTime = new GregorianCalendar();
			fromTime.set(Calendar.HOUR_OF_DAY, fromHourNum);
			fromTime.set(Calendar.MINUTE, fromMinuteNum);
			date.setFromTime(fromTime);

			
			toHourNum = Integer.parseInt(toHourTextField.getText().trim());
			toMinuteNum = Integer.parseInt(toMinuteTextField.getText().trim());
			GregorianCalendar toTime = new GregorianCalendar();
			toTime.set(Calendar.HOUR_OF_DAY, toHourNum);
			toTime.set(Calendar.MINUTE, toMinuteNum);
			date.setToTime(toTime);

			date.setDateColor(dateColor);
			
			repeat = (String) repeatComboBox.getSelectedItem();
			date.setRepeat(repeat);
			repeatTimeLimit = Integer.parseInt(repeatTimeTextField.getText().trim());
			date.setRepeatTimeLimit(repeatTimeLimit);
			
			location = locationTextField.getText().trim();
			date.setLocation(location);
			
			comment = commentTextField.getText();
			date.setComment(comment);

			mainView.save();
			mainView.updateView();
			mainView.save();
			dateDialog.setVisible(false);
			mainView.save();
		}
		
	}

}
