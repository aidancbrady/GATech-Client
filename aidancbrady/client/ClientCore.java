package aidancbrady.client;

import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import aidancbrady.client.gui.GuiClient;

public class ClientCore
{
	private static ClientCore instance;
	
	public String discussion;
	
	public Set<ClientUser> usersOnline = new HashSet<ClientUser>();
	
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
	
	public void userJoined(String username, String mod)
	{
		for(ClientUser user : usersOnline)
		{
			if(user.username.equals(username))
			{
				return;
			}
		}
		
		usersOnline.add(new ClientUser(username, mod.equals("yes") ? true : false));
	}
	
	public void setIP(String ip)
	{
		serverIP = ip.split(":")[0];
		serverPort = Integer.parseInt(ip.split(":")[1]);
		theGui.portLabel.setText(serverIP + ":" + serverPort);
		FileHandler.saveProperties();
	}
	
	public void userLeft(String username)
	{
		ClientUser toRemove = null;
		
		for(ClientUser user : usersOnline)
		{
			if(user.username.equals(username))
			{
				toRemove = user;
				break;
			}
		}
		
		usersOnline.remove(toRemove);
	}
	
	public void init()
	{
		try {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Chatter (Client)");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch(Exception e) {}
		
		try {
			theGui = new GuiClient();
			FileHandler.loadProperties();
			
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
		FileHandler.saveProperties();
	}
	
	public void updateModeratorName(String name)
	{
		for(ClientUser user : usersOnline)
		{
			if(user.isModerator)
			{
				user.username = name;
				break;
			}
		}
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
			
			theGui.clientMenu.connectItem.setEnabled(false);
			theGui.clientMenu.disconnectItem.setEnabled(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateDiscussion(String name)
	{
		if(name == null || name.equals(""))
		{
			discussion = null;
			Util.updateWithFont(theGui.discussionLabel, "Undefined", new Font("Arial", Font.BOLD, 14));
		}
		else {
			discussion = name;
			Util.updateWithFont(theGui.discussionLabel, name, new Font("Arial", Font.BOLD, 14));
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
			
			usersOnline.clear();
			updateDiscussion(null);
			theGui.chatBox.setText("Disconnected.");
			
			updateState(ConnectionState.DISCONNECTED);
			
			theGui.setPortButton.setEnabled(true);
			theGui.connectionField.setEnabled(true);
			theGui.connectButton.setEnabled(true);
			theGui.disconnectButton.setEnabled(false);
			
			theGui.clientMenu.connectItem.setEnabled(true);
			theGui.clientMenu.disconnectItem.setEnabled(false);
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
