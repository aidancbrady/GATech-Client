package aidancbrady.client.gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import aidancbrady.client.Util;

public class ClientMenu 
{
	public JMenuBar menuBar = new JMenuBar();
	
	public JMenu connectionMenu = new JMenu("Connection");
	
	public JMenuItem connectItem = new JMenuItem("Connect");
	public JMenuItem disconnectItem = new JMenuItem("Disconnect");
	
	public ClientMenu()
	{
		connectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Util.getActionKey()));
		connectionMenu.add(connectItem);
		
		disconnectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, Util.getActionKey()));
		disconnectItem.setEnabled(false);
		connectionMenu.add(disconnectItem);
		
		menuBar.add(connectionMenu);
	}
}
