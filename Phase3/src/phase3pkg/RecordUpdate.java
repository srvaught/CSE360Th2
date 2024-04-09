package phase3pkg;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
}