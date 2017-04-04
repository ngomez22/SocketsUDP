package ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import client.Client;

public class GUI extends JFrame {
	
	private Client client;
	private Desktop desktop;
	private Connection connection;

	public GUI() {
		desktop = Desktop.getDesktop();
		connection = new Connection(this);

		setSize(530, 530);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("UDP Client");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		add(connection, BorderLayout.NORTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (client != null) {
					int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to end the connection?",
							"TCP Client", JOptionPane.YES_NO_OPTION);
					if (confirmed == JOptionPane.YES_OPTION) {
						dispose();
					}
				} else {
					dispose();
				}
			}
		});
	}

	public void showPanels() {
	}

	public void hidePanels() {
	}

	public void connect() {
		String ip = connection.getIp();
		int port = connection.getPort();
		int numMessages = connection.getNumberOfMessages();
		if(port != -1 && numMessages != -1 && !ip.equals("")){
			client = new Client(ip, port, numMessages);
		}
	}

	public void download() {
	}
	
	public void stopDownload() {
	}
	
	public void open() {
	}

	public void disconnect() {
	}

	public void getDownloadables() {
	}

	public void getDownloads() {
	}

	public static void main(String[] args) {
		GUI i = new GUI();
		i.setVisible(true);
	}
}
