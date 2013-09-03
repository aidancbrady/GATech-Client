package aidancbrady.client;

import javax.swing.JOptionPane;

import aidancbrady.client.ClientCore.ConnectionState;

public class ThreadConnect extends Thread
{
	public SocketConnection connection;
	
	public ThreadConnect(SocketConnection conn)
	{
		connection = conn;
	}
	
	@Override
	public void run()
	{
		try {
			for(int i = 0; i < 200; i++)
			{
				if(connection.hasConnection)
				{
					ClientCore.instance().updateState(ConnectionState.CONNECTED);
					finalize();
					break;
				}
				
				sleep(10);
			}
			
			if(!connection.hasConnection)
			{
				ClientCore.instance().disconnect();
				JOptionPane.showMessageDialog(ClientCore.instance().theGui, "Connection timeout.", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
