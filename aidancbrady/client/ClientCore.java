package aidancbrady.client;

import javax.swing.JOptionPane;

public class ClientCore
{
	private static ClientCore instance;
	
	public ConnectionState state = ConnectionState.DISCONNECTED;
	
	public String username = "";
	
	public SocketConnection activeConnection;
	
	public String serverIP = "0.0.0.0";
	
	public int serverPort = -1;
	
	public GuiClient theGui;
	
	public static void main(String[] args)
	{
		instance = new ClientCore();
		instance.init();
	}
	
	public void init()
	{
		try {
			theGui = new GuiClient();
			
			synchronized(this)
			{
				wait();
			}
			
			if(activeConnection != null && state == ConnectionState.CONNECTED)
			{
				activeConnection.socket.close();
			}
			
			System.exit(0);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void updateState(ConnectionState connection)
	{
		state = connection;
		theGui.connectedLabel.setText(state.friendly + " -");
	}
	
	public void setUsername(String name)
	{
		username = name;
		theGui.usernameLabel.setText(name);
	}
	
	public void connect()
	{
		if(serverPort == -1 || serverIP.equals("0.0.0.0"))
		{
			JOptionPane.showMessageDialog(theGui, "Please define an IP and port before continuing.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(username.equals(""))
		{
			JOptionPane.showMessageDialog(theGui, "Please define a username before continuing.", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			(activeConnection = new SocketConnection()).start();
			
			updateState(ConnectionState.CONNECTING);
			
			theGui.setPortButton.setEnabled(false);
			theGui.connectionField.setEnabled(false);
			theGui.connectButton.setEnabled(false);
			theGui.disconnectButton.setEnabled(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		try {
			if(activeConnection == null)
			{
				return;
			}
			
			if(activeConnection.socket != null)
			{
				activeConnection.socket.close();
			}
			
			updateState(ConnectionState.DISCONNECTED);
			
			theGui.setPortButton.setEnabled(true);
			theGui.connectionField.setEnabled(true);
			theGui.connectButton.setEnabled(true);
			theGui.disconnectButton.setEnabled(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ClientCore instance()
	{
		return instance;
	}
	
	public static enum ConnectionState
	{
		CONNECTED("yes", "Connected"),
		DISCONNECTED("no", "Idle"),
		CONNECTING("in progress", "Connecting");
		
		public String description;
		public String friendly;
		
		private ConnectionState(String desc, String friend)
		{
			description = desc;
			friendly = friend;
		}
	}
}
