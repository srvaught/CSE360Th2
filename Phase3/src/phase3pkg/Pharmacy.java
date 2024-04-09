package phase3pkg;
import java.util.Map;
public class Pharmacy 
{
    private String pharmacyID;
    private String name;
    private String address;
    private String phone;
    public Pharmacy(String pharmacyID, String name, String address, String phone) 
    {
        this.pharmacyID = pharmacyID;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
    public void updateInformation(Map<String, String> newInfo) 
    {
        if(newInfo.containsKey("name")) 
        {
            this.name = newInfo.get("name");
        }
        if(newInfo.containsKey("address")) 
        {
            this.address = newInfo.get("address");
        }
        if(newInfo.containsKey("phone")) 
        {
            this.phone = newInfo.get("phone");
        }
    } 
}