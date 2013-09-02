package aidancbrady.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GuiClient extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private JTextArea chatBox;
	
	private JList statistics;
	
	public JList onlineUsersList;
	
	public JPanel serverControlPanel;
	
	public JButton setPortButton;
	
	public JButton startServerButton;
	
	public JButton stopServerButton;
	
	public JTextField portEntry;
	
	public JLabel portLabel;
	
	public JLabel connectedLabel;
	
	public JTextField chatField;
	
	public boolean isOpen = true;
	
	public Timer timer;
	
	public GuiClient()
	{
		super("DynamicServer (Client)");
		
		timer = new Timer(100, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				int onlineIndex = onlineUsersList.getSelectedIndex();
				
				Vector<String> userVector = new Vector<String>();
				
				/*for(ServerConnection connection : ServerCore.instance().connections.values())
				{
					userVector.add(connection.getUserID() + ": " + (connection.isAuthenticated() ? connection.user.username : "Guest"));
				}*/
				
				if(userVector.isEmpty())
				{
					userVector.add("No users online.");
				}
				
				onlineUsersList.setListData(userVector);
				onlineUsersList.setSelectedIndex(onlineIndex);
				
				if(userVector.size() == 1)
				{
					onlineUsersList.setSelectedIndex(0);
				}
				
				Vector<String> statsVector = new Vector<String>();
				statsVector.add("Connected: " + ClientCore.instance().connected);
				//statsVector.add("Online count: " + ServerCore.instance().connections.size());
				statsVector.add("Active threads: " + Thread.activeCount());
				statsVector.add("Active memory: " + (int)((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000) + "MB");
				statsVector.add("Total memory: " + (int)(Runtime.getRuntime().totalMemory()/1000000) + "MB");
				
				statistics.setListData(statsVector);
			}
		});
		
		timer.start();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception localException) {}
		
		JPanel completePanel = new JPanel(new BorderLayout());
		
		JPanel rightInfoPanel = new JPanel(new BorderLayout());
		rightInfoPanel.setPreferredSize(new Dimension(206, 800));
		
		JPanel leftInfoPanel = new JPanel(new BorderLayout());
		leftInfoPanel.setPreferredSize(new Dimension(206, 800));
		
		setBackground(Color.LIGHT_GRAY);
		setResizable(false);
		
		//Start user list panel
		onlineUsersList = new JList();
		
		/*onlineUsersList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					if(!((String)onlineUsersList.getSelectedValue()).equals("No users online."))
					{
						int id = Integer.parseInt(((String)onlineUsersList.getSelectedValue()).split(":")[0]);
						ServerConnection connection = ServerCore.instance().connections.get(id);
						
						if(connection != null)
						{
							if(!connection.isAuthenticated())
							{
								new GuiConnectionInfo(id);
							}
							else {
								new GuiCacheInfo(connection.user.username);
							}
						}
					}
				}
			}
		});*/
		
		onlineUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		onlineUsersList.setBorder(new TitledBorder(new EtchedBorder(), "Online Users"));
		onlineUsersList.setVisible(true);
		onlineUsersList.setFocusable(true);
		onlineUsersList.setEnabled(true);
		onlineUsersList.setSelectionInterval(1, 1);
		onlineUsersList.setBackground(Color.GRAY);
		onlineUsersList.setToolTipText("The users currently connected to this server.");
		JScrollPane onlinePane = new JScrollPane(onlineUsersList);
		leftInfoPanel.add(onlinePane);
		//End user list panel
		
		//Start port setter panel
		serverControlPanel = new JPanel();
		serverControlPanel.setBorder(new TitledBorder(new EtchedBorder(), "Server Control"));
		serverControlPanel.setVisible(true);
		serverControlPanel.setBackground(Color.GRAY);
		serverControlPanel.setFocusable(false);
		serverControlPanel.setPreferredSize(new Dimension(206-15, 290));
		serverControlPanel.setToolTipText("Set this server's active port to a new value.");
		
		connectedLabel = Util.getWithFont(new JLabel("Idle -"), new Font("Arial", Font.BOLD, 14));
		serverControlPanel.add(connectedLabel);
		
		portLabel = new JLabel("N/A");
		serverControlPanel.add(portLabel);
		
		portEntry = new JTextField();
		portEntry.setFocusable(true);
		portEntry.setText("");
		portEntry.setPreferredSize(new Dimension(140, 20));
		portEntry.addActionListener(new SetPortListener());
		serverControlPanel.add(portEntry, "North");
		
		setPortButton = new JButton("Confirm");
		setPortButton.setFocusable(true);
		setPortButton.setPreferredSize(new Dimension(120, 25));
		setPortButton.addActionListener(new SetPortListener());
		
		serverControlPanel.add(setPortButton, "Center");
		
		startServerButton = new JButton("Connect");
		startServerButton.setFocusable(true);
		startServerButton.setPreferredSize(new Dimension(80, 25));
		startServerButton.setEnabled(true);
		startServerButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ClientCore.instance().connect();
			}
		});
		
		serverControlPanel.add(startServerButton, "South");
		
		stopServerButton = new JButton("Disconnect");
		stopServerButton.setFocusable(true);
		stopServerButton.setPreferredSize(new Dimension(80, 25));
		stopServerButton.setEnabled(false);
		stopServerButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ClientCore.instance().disconnect();
			}
		});
	
		serverControlPanel.add(stopServerButton, "South");
		
		rightInfoPanel.add(serverControlPanel, "North");
		//End port setter panel
		
		//Start statistics panel
		statistics = new JList();
		
		for(MouseListener listener : statistics.getMouseListeners())
		{
			statistics.removeMouseListener(listener);
		}
		
		statistics.setBorder(new TitledBorder(new EtchedBorder(), "Statistics"));
		statistics.setVisible(true);
		statistics.setBackground(Color.GRAY);
		statistics.setFocusable(false);
		statistics.setPreferredSize(new Dimension(206-15, 164));
		statistics.setToolTipText("Statistics regarding this server.");
		rightInfoPanel.add(new JScrollPane(statistics));
		//End statistics panel
		
		completePanel.add(rightInfoPanel, "West");
		completePanel.add(leftInfoPanel, "East");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		//Start chat box panel
		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBorder(new TitledBorder(new EtchedBorder(), "Chatbox"));
		chatBox.setAutoscrolls(true);
		chatBox.setBackground(Color.LIGHT_GRAY);
		mainPanel.add(new JScrollPane(chatBox), "Center");
		//End chat box panel
		
		JPanel chatEntryPanel = new JPanel(new BorderLayout());
		chatEntryPanel.setBackground(Color.WHITE);
		
		//Start chat field panel
		chatField = new JTextField();
		chatField.setFocusable(true);
		chatField.setText("");
		chatField.setPreferredSize(new Dimension(120, 40));
		chatField.addActionListener(new ChatBoxListener());
		chatField.setBorder(new TitledBorder(new EtchedBorder(), "Type here to Chat"));
		chatEntryPanel.add(chatField, "Center");
		//End chat field panel
		
		JButton clearChatButton = new JButton("Clear");
		clearChatButton.setVisible(true);
		clearChatButton.setBackground(Color.WHITE);
		clearChatButton.setFocusable(true);
		clearChatButton.setPreferredSize(new Dimension(60, 40));
		clearChatButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				chatBox.setText("Chat cleared.");
				appendChat("");
			}
		});
		chatEntryPanel.add(clearChatButton, "East");
		
		mainPanel.add(chatEntryPanel, "South");
		
		completePanel.add(mainPanel, "Center");
		add(completePanel);
		
		addWindowListener(this);
		setSize(854, 580);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}		

	public void appendChat(String str) 
	{	
		chatBox.append(str+"\n");	
		chatBox.setCaretPosition(chatBox.getText().length() - 1);
	}
	
	public class SetPortListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(!ClientCore.instance().connected)
			{
				String command = portEntry.getText().trim().toLowerCase();
				
				if(command == null || command.equals(""))
				{
					return;
				}
				
				try {
					String[] split = command.split(":");
					
					if(split.length != 2 || !Util.isVaildIP(split[0]))
					{
						JOptionPane.showMessageDialog(GuiClient.this, "Please enter a valid IPv4 address.\nX.X.X.X:X");
						portEntry.setText("");
						return;
					}
					
					ClientCore.instance().serverIP = split[0];
					ClientCore.instance().serverPort = Integer.parseInt(split[1]);
					portLabel.setText(ClientCore.instance().serverIP + ":" + ClientCore.instance().serverPort);
					portEntry.setText("");
				} catch(Exception e) {
					JOptionPane.showMessageDialog(GuiClient.this, "Invalid characters.", "Warning", JOptionPane.WARNING_MESSAGE);
					portEntry.setText("");
				}
			}
		}
	}

	public class ChatBoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			try {
				chatField.setText("");
				String command = arg0.getActionCommand().trim().toLowerCase();
				
				if(command == null || command.equals(""))
				{
					return;
				}
				
				if(command.startsWith("/"))
				{
					command = command.substring(1);
					String[] commandArgs = command.split(" ");
					
					if(command.equals("quit"))
					{
						windowClosing(null);
					}
					else {
						appendChat("Unknown command.");
					}
				}
				else {
					if(ClientCore.instance().connected)
					{
						appendChat("You: " + command);
						ClientCore.instance().activeConnection.printWriter.println(command);
					}
				}
			} catch (Exception e) {
				appendChat("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e)
	{
		timer.stop();
		isOpen = false;
		
		synchronized(ClientCore.instance())
		{
			ClientCore.instance().notify();
		}
	}
	
	@Override
	public void windowClosed(WindowEvent e) {}
	
	@Override
	public void windowOpened(WindowEvent e) {}
	
	@Override
	public void windowIconified(WindowEvent e) {}
	
	@Override
	public void windowDeiconified(WindowEvent e) {}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	
	@Override
	public void windowDeactivated(WindowEvent e) {}
}
