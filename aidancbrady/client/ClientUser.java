package aidancbrady.client;

public class ClientUser
{
	public String username;
	public boolean isModerator = false;
	
	public ClientUser(String name)
	{
		username = name;
	}
	
	public ClientUser(String name, boolean mod)
	{
		this(name);
		
		isModerator = mod;
	}
}
