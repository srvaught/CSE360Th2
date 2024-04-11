package evenRecognizerTestbed;
import java.util.*;
public class RecordUpdate 
{
    private Date updateDate;
    private Map<String, String> updateFields;
    private String updaterID;
    public RecordUpdate(String updaterID) 
    {
        this.updaterID = updaterID;
        this.updateDate = new Date(); 
        this.updateFields = new HashMap<>();
    }
    public void addUpdate(String field, String value) 
    {
        updateFields.put(field, value);
    }
    public void removeUpdate(String field) 
    {
        if(updateFields.containsKey(field)) 
        {
            updateFields.remove(field);
        } 
    }
    public Date getUpdateDate()
    {
    	return updateDate;
    }
    public Map<String, String> getUpdateFields()
    {
    	return updateFields;
    }
    public String getUpdaterID()
    {
    	return updaterID;
    }
}