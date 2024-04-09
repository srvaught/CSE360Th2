package phase3pkg;
public class User
{
	private String userID;
	private String password;
	private String name;
	private String contactInformation;
	private boolean loggedIn = false;
	public User(String userID, String password, String name, String contactInformation)
	{
		this.userID = userID;
		this.password = password;
		this.name = name;
		this.contactInformation = contactInformation;
	}
	public boolean login(String userID, String password)
	{
		if(this.userID.equals(userID) && this.password.equals(password))
		{
			loggedIn = true;
			return true;
		}
		else
		{
			return false;
		}
	}
	public void logout()
	{
		if(loggedIn)
		{
			loggedIn = false;
		}
	}
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
}