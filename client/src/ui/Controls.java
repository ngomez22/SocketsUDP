package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Controls extends JPanel implements ActionListener {

	public static final String DOWNLOAD = "DOWNLOAD";
	public static final String STOP = "STOP";

	private GUI gui;
	private JButton downloadBtn;
	private JButton stopBtn;

	public Controls(GUI gui) {
		this.gui = gui;
		setLayout(new GridLayout(1, 2));
		setBorder(new TitledBorder("Controls"));

		downloadBtn = new JButton("Download");
		downloadBtn.addActionListener(this);
		downloadBtn.setActionCommand(DOWNLOAD);
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(this);
		stopBtn.setActionCommand(STOP);
		stopBtn.setEnabled(true);

		add(downloadBtn);
		add(stopBtn);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals(DOWNLOAD)) {
			gui.download();
		}
		if (arg0.getActionCommand().equals(STOP)) {
			gui.stopDownload();
		}
	}
}
