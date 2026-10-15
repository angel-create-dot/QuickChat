package za.com.softchat.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the details of one QuickChat message.
 *
 * @author Angel Qosha ST10533417
 */
public class Message {

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private static final String STORAGE_FILE = "storedMessages.json";

    /**
     * Creates a message with all of its details.
     */
    public Message(String messageID, int messageNumber, String recipient,
            String messageText, String messageHash) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;

    }

    /**
     * Stores this message in the JSON file so it can be sent later. The
     * existing stored messages are read first so none are overwritten. Adapted
     * from the Gson User Guide: Google, n.d. Gson User Guide. Available at:
     * https://github.com/google/gson/blob/main/UserGuide.md [Accessed 15
     * October 2026].
     *
     * @return true if the message was stored, false if the file could not be
     * written
     */
    public boolean storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Message> storedMessages = new ArrayList<>();
        File storageFile = new File(STORAGE_FILE);

        try {
            if (storageFile.exists()) {
                try (FileReader reader = new FileReader(storageFile)) {
                    Type listType = new TypeToken<List<Message>>() {
                    }.getType();
                    List<Message> existingMessages = gson.fromJson(reader, listType);
                    if (existingMessages != null) {
                        storedMessages = existingMessages;
                    }
                }
            }

            storedMessages.add(this);

            try (FileWriter writer = new FileWriter(storageFile)) {
                gson.toJson(storedMessages, writer);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
