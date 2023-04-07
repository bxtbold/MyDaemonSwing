package com.ur.urcap.examples.mydaemonswing.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class XmlRpcMyDaemonInterface {

	private final XmlRpcClient client;

	public XmlRpcMyDaemonInterface(String host, int port) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setEnabledForExtensions(true);
		try {
			config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		config.setConnectionTimeout(1000); // 1s
		client = new XmlRpcClient();
		client.setConfig(config);
	}

	public boolean isReachable() {
		try {
			client.execute("is_alive", new ArrayList<String>());
			return true;
		} catch (XmlRpcException e) {
			return false;
		}
	}

	public int target_action(String ip, String port) throws XmlRpcException, UnknownResponseException {
		ArrayList<String> args = new ArrayList<String>();
		args.add(ip);
		args.add(port);
		Object result = client.execute("target_action", args);
		return processInteger(result);
	}

	// private String processString(Object response) throws UnknownResponseException {
	// 	if (response instanceof String) {
	// 		return (String) response;
	// 	} else {
	// 		throw new UnknownResponseException();
	// 	}
	// }

	private Integer processInteger(Object response) throws UnknownResponseException {
		if (response instanceof Integer) {
			return (Integer) response;
		} else {
			throw new UnknownResponseException();
		}
	}
}
