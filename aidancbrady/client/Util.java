package aidancbrady.client;

import java.awt.Font;

import javax.swing.JLabel;

public final class Util 
{
	public static JLabel getWithFont(JLabel label, Font font)
	{
		label.setFont(font);
		return label;
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
}
