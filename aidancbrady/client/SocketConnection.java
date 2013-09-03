package aidancbrady.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class SocketConnection extends Thread
{
	public Socket socket;
	
	public BufferedReader bufferedReader;
	
	public PrintWriter printWriter;
	
	@Override
	public void run()
	{
		try {
			InetAddress address = InetAddress.getByName(ClientCore.instance().serverIP);
			socket = new Socket(address, ClientCore.instance().serverPort);
			
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			
			printWriter.println("/AUTH:" + ClientCore.instance().username);
			
			String readerLine = "";
			boolean doneReading = false;
			
			while((readerLine = bufferedReader.readLine()) != null && !doneReading)
			{
				if(readerLine.trim().startsWith("/warning"))
				{
					String[] split = readerLine.trim().split(":");
					JOptionPane.showMessageDialog(ClientCore.instance().theGui, split[1], "Warning", JOptionPane.WARNING_MESSAGE);
					ClientCore.instance().disconnect();
					return;
				}
				
				ClientCore.instance().theGui.appendChat(readerLine.trim());
			}
			
			printWriter.close();
			socket.close();
			
			ClientCore.instance().connected = false;
			
			finalize();
		} catch(SocketException e) {
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
