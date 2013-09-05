package aidancbrady.client;

import java.util.ArrayList;

public class ClientUser
{
	public String username;
	public ArrayList<String> messages;
	
	public ClientUser(String name)
	{
		username = name;
		messages = new ArrayList<String>();
	}
}
