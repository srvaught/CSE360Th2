package evenRecognizerTestbed;
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
	public String getName()
	{
		return name;
	}
	public String getUserID()
	{
		return userID;
	}
	public String getPassword()
	{
		return password;
	}
	public String getContactInformation()
	{
		return contactInformation;
	}
	public void setName(String newName) 
    {
        this.name = newName;
    }
    public void setContactInformation(String newContactInformation) 
    {
        this.contactInformation = newContactInformation;
    }
}