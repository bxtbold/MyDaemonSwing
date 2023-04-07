package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;


public class MyDaemonInstallationNodeContribution implements InstallationNodeContribution {
	private static final String XMLRPC_VARIABLE = "my_daemon_swing";
	public static final int PORT = 40405;

	private final MyDaemonDaemonService daemonService;
	private XmlRpcMyDaemonInterface xmlRpcDaemonInterface;

	public MyDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider, MyDaemonInstallationNodeView view, DataModel model, MyDaemonDaemonService daemonService, CreationContext context) {
		this.daemonService = daemonService;
		xmlRpcDaemonInterface = new XmlRpcMyDaemonInterface("127.0.0.1", PORT);
		applyDesiredDaemonStatus();
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.assign(XMLRPC_VARIABLE, "rpc_factory(\"xmlrpc\", \"http://127.0.0.1:" + PORT + "/RPC2\")");
	}

	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Download the daemon settings to the daemon process on initial start for real-time preview purposes
				try {
					awaitDaemonRunning();
				} catch(Exception e){
					System.err.println("Could not set the title in the daemon process.");
				} finally {
				}
			}
		}).start();
	}

	private void awaitDaemonRunning() throws InterruptedException {
		daemonService.getDaemon().start();
		long endTime = System.nanoTime() + 5000 * 1000L * 1000L;
		while(System.nanoTime() < endTime && (daemonService.getDaemon().getState() != DaemonContribution.State.RUNNING || !xmlRpcDaemonInterface.isReachable())) {
			Thread.sleep(100);
		}
	}

	public boolean isDefined() {
		return getDaemonState() == DaemonContribution.State.RUNNING;
	}

	public DaemonContribution.State getDaemonState() {
		return daemonService.getDaemon().getState();
	}

	public String getXMLRPCVariable(){
		return XMLRPC_VARIABLE;
	}

	public XmlRpcMyDaemonInterface getXmlRpcDaemonInterface() {
		// This method can be used as the following:
		// getInstallation().getXmlRpcDaemonInterface().target_action();
		return xmlRpcDaemonInterface;
	}
}
