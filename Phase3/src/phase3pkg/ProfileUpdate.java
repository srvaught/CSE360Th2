package phase3pkg;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class ProfileUpdate 
{
    private Date updateDate;
    private Map<String, String> updatedFields;
    public ProfileUpdate() 
    {
        this.updateDate = new Date();
        this.updatedFields = new HashMap<>();
    }
    public void addUpdate(String field, String value) 
    {
        updatedFields.put(field, value);
    }
    public void removeUpdate(String field) 
    {
        if(updatedFields.containsKey(field)) 
        {
            updatedFields.remove(field);
        }
    }
}