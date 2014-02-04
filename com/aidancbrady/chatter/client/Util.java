package com.aidancbrady.chatter.client;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JLabel;

public final class Util 
{
	public static JLabel getWithFont(JLabel label, Font font)
	{
		label.setFont(font);
		return label;
	}
	
	public static void updateWithFont(JLabel label, String text, Font font)
	{
		label.setText(text);
		label.setFont(font);
	}
	
	public static String getMessage(String toSplit)
	{
		StringBuilder builder = new StringBuilder();
		boolean foundSplitter = false;
		
		for(Character c : toSplit.toCharArray())
		{
			if(!foundSplitter)
			{
				if(c.equals(':'))
				{
					foundSplitter = true;
					continue;
				}
			}
			else {
				builder.append(c);
			}
		}
		
		return builder.toString();
	}
	
	public static boolean isValidMessage(String message)
	{
		if(message.length() > 500)
		{
			return false;
		}
		
		return true;
	}
	
	public static String trimMessage(String message)
	{
		StringBuilder builder = new StringBuilder();
		int charCount = 0;
		
		for(Character c : message.toCharArray())
		{
			charCount++;
			
			if(charCount == 50)
			{
				builder.append("\n");
			}
			
			builder.append(c);
		}
		
		return builder.toString();
	}
	
	public static boolean isValidUsername(String username)
	{
		if(username.length() > 16)
		{
			return false;
		}
		
		for(Character c : username.toCharArray())
		{
			if(!Character.isLetter(c) && !Character.isDigit(c))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isVaildIP(String ip)
	{
		if(ip.equals("localhost"))
		{
			return true;
		}
		
		ip = ip.replace(".", ":");
		String[] split = ip.split(":");
		
		if(split.length != 4)
		{
			return false;
		}
		
		for(String s : split)
		{
			try {
				Integer.parseInt(s);
				continue;
			} catch(NumberFormatException e) {
				return false;
			}
		}
		
		return true;
	}
	
	public static int getActionKey()
	{
		return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	}
}
