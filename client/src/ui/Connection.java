package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

public class Connection extends JPanel implements ActionListener {
	
	public static final String CONNECT = "CONNECT";
	public static final String DISCONNECT = "DISCONNECT";
	
	private GUI gui;
	private JLabel ipLabel;
	private JTextField ipText;
	private JLabel portLabel;
	private JTextField portText;
	private JLabel statusLabel;
	private JTextField statusText;
	private JButton connect;
	
	public Connection (GUI gui) {
		this.gui = gui;
		setLayout( new BorderLayout() );
	    setBorder( new TitledBorder( "Connection" ) );
	    
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
	    
	    statusLabel = new JLabel("Estado: ");
	    statusText = new JTextField();
	    statusText.setEditable(false);
	    
	    JPanel status = new JPanel(new BorderLayout());
	    status.add(statusLabel, BorderLayout.WEST);
	    status.add(statusText, BorderLayout.CENTER);
	    
	    JPanel connection = new JPanel(new GridLayout(2,1));
	    connection.add(options);
	    connection.add(status);
	    
	    connect = new JButton("");
	    connectButton();
	    connect.addActionListener(this);
	    
	    add(connect, BorderLayout.EAST);
	    add(connection);
	}
	
	public void changeStatus(String newState) {
		statusText.setText(newState);
	}
	
	public void connectButton() {
		connect.setText("Conectar");
		connect.setBackground(new Color(60, 200, 100));
	    connect.setActionCommand(CONNECT);
	    revalidate();
	    repaint();
	}
	
	public void disconnectButton() {
		connect.setText("Desconectar");
	    connect.setBackground(new Color(190, 60, 60));
	    connect.setActionCommand(DISCONNECT);
	    revalidate();
	    repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CONNECT)) {
			gui.connect();
		}
		if(e.getActionCommand().equals(DISCONNECT)) {
			gui.disconnect();
		}
	}
}
