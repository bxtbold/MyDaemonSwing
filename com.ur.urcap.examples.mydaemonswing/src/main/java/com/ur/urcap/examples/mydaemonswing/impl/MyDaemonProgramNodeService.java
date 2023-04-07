package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;


public class MyDaemonProgramNodeService implements SwingProgramNodeService<MyDaemonProgramNodeContribution, MyDaemonProgramNodeView> {

	public MyDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "TestDaemonSwingNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Test Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public MyDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new MyDaemonProgramNodeView();
	}

	@Override
	public MyDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, MyDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new MyDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
