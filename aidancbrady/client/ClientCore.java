package aidancbrady.client;

public class ClientCore
{
	private static ClientCore instance;
	
	public boolean connected = false;
	
	public String username;
	
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
		} catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void connect()
	{
		if(serverPort == -1 || serverIP.equals("0.0.0.0"))
		{
			return;
		}
		
		try {
			(activeConnection = new SocketConnection()).start();
			
			connected = true;
			
			theGui.connectedLabel.setText("Connected -");
			
			theGui.setPortButton.setEnabled(false);
			theGui.portEntry.setEnabled(false);
			theGui.startServerButton.setEnabled(false);
			theGui.stopServerButton.setEnabled(true);
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
			theGui.portEntry.setEnabled(true);
			theGui.startServerButton.setEnabled(true);
			theGui.stopServerButton.setEnabled(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ClientCore instance()
	{
		return instance;
	}
}
