package phase3pkg;
import java.util.Date;
public class Message 
{
    private String messageID;
    private String senderID;
    private String receiverID;
    private String content;
    private Date sentDate;
    private boolean isRead;
    public Message(String messageID, String senderID, String receiverID, String content) 
    {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.sentDate = null; 
        this.isRead = false;
    }
    public void sendMessage() 
    {
        this.sentDate = new Date();
        //TODO
    }
    public void markAsRead() 
    {
        this.isRead = true;
        //TODO
    }
}