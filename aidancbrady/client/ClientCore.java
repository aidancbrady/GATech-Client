package aidancbrady.client;

import javax.swing.JOptionPane;

public class ClientCore
{
	private static ClientCore instance;
	
	public boolean connected = false;
	
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
			
			if(activeConnection != null && connected)
			{
				activeConnection.socket.close();
			}
			
			System.exit(0);
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setUsername(String name)
	{
		username = name;
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
			
			connected = true;
			
			theGui.connectedLabel.setText("Connected -");
			
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
			activeConnection.socket.close();
			
			connected = false;
			
			theGui.connectedLabel.setText("Idle -");
			
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
}
