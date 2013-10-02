package aidancbrady.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JOptionPane;

public final class FileHandler 
{
	public static final File discussionsDir = new File(getHomeDirectory() + "/Documents/Discussions/Client");
	
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
}
