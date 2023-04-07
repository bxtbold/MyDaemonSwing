package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.*;
import java.awt.*;


public class MyDaemonProgramNodeView implements SwingProgramNodeView<MyDaemonProgramNodeContribution> {

	public MyDaemonProgramNodeView() {
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<MyDaemonProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createInfo());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoBox.add(new JLabel("The program is running."));
		return infoBox;
	}
}
