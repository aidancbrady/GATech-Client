package com.aidancbrady.chatter.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import javax.swing.JOptionPane;

public final class FileHandler 
{
	public static final File discussionsDir = new File(getHomeDirectory() + "/Documents/Chatter/Discussions/Client");
	public static final File dataDir = new File(getHomeDirectory() + "/Documents/Chatter/Data/Client");
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
	
	public static void saveDiscussion()
	{
		try {
			if(!discussionsDir.exists())
			{
				discussionsDir.mkdir();
			}
			
			File file = new File(discussionsDir.getAbsolutePath() + "/" + ClientCore.instance().discussion + ".disc");
			
			if(file.exists())
			{
				file.delete();
			}
			
			file.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			writer.append(ClientCore.instance().theGui.chatBox.getText());
			
			writer.flush();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void openDiscussion(File file)
	{
		try {
			if(!file.exists())
			{
				return;
			}
			
			if(!file.getAbsolutePath().endsWith(".disc"))
			{
				JOptionPane.showMessageDialog(ClientCore.instance().theGui, "Please select a valid '.disc' discussion file to load.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			StringBuilder builder = new StringBuilder();
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String readingLine;
			
			while((readingLine = reader.readLine()) != null)
			{
				builder.append(readingLine);
				builder.append("\n");
			}
			
			ClientCore.instance().updateDiscussion(file.getName().replace(".", ":").split(":")[0]);
			ClientCore.instance().theGui.chatBox.setText(builder.toString());
			
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadProperties()
	{
		try {
			Properties properties = new Properties();
			
			if(!dataDir.exists())
			{
				dataDir.mkdirs();
			}
			
			File file = new File(dataDir.getAbsolutePath() + "/CachedProps.txt");
			
			if(!file.exists())
			{
				return;
			}
			
			properties.load(new FileInputStream(file));
			
			if(properties.containsKey("ip"))
			{
				ClientCore.instance().setIP(properties.getProperty("ip"));
			}
			
			if(properties.containsKey("username"))
			{
				ClientCore.instance().setUsername(properties.getProperty("username"));
			}
		} catch(Exception e) {
			System.out.println("An error ocurred while reading properties.");
		}
	}
	
	public static void saveProperties()
	{
		try {
			Properties properties = new Properties();
			
			if(!dataDir.exists())
			{
				dataDir.mkdirs();
			}
			
			File file = new File(dataDir.getAbsolutePath() + "/CachedProps.txt");
			
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileOutputStream outputStream = new FileOutputStream(file);
			
			if(ClientCore.instance().serverIP != null)
			{
				properties.setProperty("ip", ClientCore.instance().serverIP + ":" + ClientCore.instance().serverPort);
			}
			
			if(ClientCore.instance().username != null)
			{
				properties.setProperty("username", ClientCore.instance().username);
			}
			
			properties.store(outputStream, "Server Cached Properties");
			outputStream.close();
		} catch(Exception e) {
			System.out.println("An error ocurred while saving properties.");
		}
	}
}
