package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

public class Connection extends JPanel implements ActionListener {
	
	public static final String SEND = "SEND";
	
	private GUI gui;
	private JLabel ipLabel;
	private JTextField ipText;
	private JLabel portLabel;
	private JTextField portText;
	private JLabel messagesLabel;
	private JSpinner messagesText;
	private JButton connect;
	
	public Connection (GUI gui) {
		this.gui = gui;
		setLayout( new BorderLayout() );
		setBorder(new TitledBorder("Configuration"));
	    
	    ipLabel = new JLabel("IP:");
	    ipText = new JTextField("127.0.0.1");
	    JPanel ipPanel = new JPanel(new GridLayout(2, 1));
	    ipPanel.add(ipLabel);
	    ipPanel.add(ipText);
	    
	    portLabel = new JLabel("Port:                 ");
	    portText = new JTextField("4321");
	    JPanel portPanel = new JPanel(new GridLayout(2, 1));
	    portPanel.add(portLabel);
	    portPanel.add(portText);
	    
	    JPanel options = new JPanel(new BorderLayout());
	    options.add(ipPanel, BorderLayout.CENTER);
	    options.add(portPanel, BorderLayout.EAST);
	    
	    SpinnerModel model = new SpinnerNumberModel(100, 10, 10000, 5);     
	    messagesText = new JSpinner(model);
	    messagesLabel = new JLabel("Number of objects to send:");
	    
	    JPanel objects = new JPanel(new BorderLayout());
	    objects.add(messagesLabel, BorderLayout.NORTH);
	    objects.add(messagesText, BorderLayout.CENTER);
	    
	    JPanel connection = new JPanel(new GridLayout(2,1));
	    connection.add(options);
	    connection.add(objects);
	    
	    connect = new JButton("Send");
	    connectButton();
	    connect.addActionListener(this);
	    
	    add(connect, BorderLayout.EAST);
	    add(connection);
	}
	
	public void connectButton() {
		connect.setText("Send");
		connect.setBackground(new Color(60, 200, 100));
	    connect.setActionCommand(SEND);
	    revalidate();
	    repaint();
	}
	
	public int getNumberOfMessages() {
		int num = -1;
		try {
			num = (Integer)messagesText.getValue();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(gui, "Please select a valid number of messages", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		return num;
	}
	
	public String getIp() {
		return ipText.getText();
	}
	
	public int getPort() {
		int num = -1;
		try {
			num = Integer.parseInt(portText.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(gui, "Please select a valid port", "Error", JOptionPane.ERROR_MESSAGE); 
		}
		return num;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(SEND)) {
			gui.connect();
		}
	}
}
