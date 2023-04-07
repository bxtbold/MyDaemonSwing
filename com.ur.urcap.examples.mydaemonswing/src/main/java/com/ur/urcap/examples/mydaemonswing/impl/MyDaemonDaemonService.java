package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.DaemonService;

import java.net.MalformedURLException;
import java.net.URL;


public class MyDaemonDaemonService implements DaemonService {

	private DaemonContribution daemonContribution;

	public MyDaemonDaemonService() {
	}

	@Override
	public void init(DaemonContribution daemonContribution) {
		this.daemonContribution = daemonContribution;
		try {
			daemonContribution.installResource(new URL("file:daemon/"));
		} catch (MalformedURLException e) {	}
	}

	@Override
	public URL getExecutable() {
		try {
			return new URL("file:daemon/daemon.py"); // Python executable
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public DaemonContribution getDaemon() {
		return daemonContribution;
	}
}
