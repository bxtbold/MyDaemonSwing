
package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;


public class MyDaemonProgramNodeContribution implements ProgramNodeContribution {
	private final ProgramAPIProvider apiProvider;

	public MyDaemonProgramNodeContribution(ProgramAPIProvider apiProvider, MyDaemonProgramNodeView view, DataModel model) {
		this.apiProvider = apiProvider;
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {}

	@Override
	public String getTitle() {
		return "Daemon Test";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Interact with the daemon process through XML-RPC calls
		// Note, alternatively plain sockets can be used.
		writer.assign("target_action", getInstallation().getXMLRPCVariable() + ".target_action()");
		writer.appendLine("textmsg(\"received target_action: \", target_action)");
		writer.appendLine("sleep(1)");
	}

	private MyDaemonInstallationNodeContribution getInstallation(){
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}
}
