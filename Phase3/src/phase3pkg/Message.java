package phase3pkg;
import java.io.*;
import java.util.*;
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
    private void saveToFile() 
    {
        String filename = "Data/Messages/" + this.receiverID + "_messages.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) 
        {
            writer.write("MessageID: " + this.messageID + "\n");
            writer.write("SenderID: " + this.senderID + "\n");
            writer.write("ReceiverID: " + this.receiverID + "\n");
            writer.write("Content: " + this.content + "\n");
            writer.write("SentDate: " + this.sentDate.toString() + "\n");
            writer.write("IsRead: " + this.isRead + "\n");
            writer.write("----\n");
        } 
        catch (IOException e) 
        {
            System.err.println("Failed to save message: " + e.getMessage());
        }
    }
    public void sendMessage() 
    {
        this.sentDate = new Date();
        saveToFile();
    }
    public void markAsRead() 
    {
        this.isRead = true;
        
    }
}
