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

    private static final String STORAGE_FILE = "storedMessages.json";
    private static final int MAX_MESSAGE_LENGTH = 250;
    private static final List<Message> sentMessages = new ArrayList<>();
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    /**
     * Creates a message; the message hash is created automatically.
     */
    public Message(String messageID, int messageNumber, String recipient,
            String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
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
     * Checks that the message ID is not more than ten characters long.
     *
     * @return true if the message ID is valid
     */
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }
        /**
     * Generates a random 10-digit message ID. A longer random number is created
     * as text and the first ten digits are taken with substring.
     *
     * @return a 10-digit message ID
     */
    public static String generateMessageID() {
        String randomDigits = String.valueOf(10_000_000_000_000L
                + (long) (Math.random() * 90_000_000_000_000L));
        return randomDigits.substring(0, 10);
    }

    /**
     * Checks the recipient's cell number by reusing the Login cell number
     * check.
     *
     * @return the capture message if the number is valid, otherwise the error
     * message
     */
    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Checks that a message is not more than 250 characters long.
     *
     * @param messageText the message to check
     * @return "Message ready to send." or how many characters the message is
     * over by
     */
    public static String checkMessageLength(String messageText) {
        int excess = messageText.length() - MAX_MESSAGE_LENGTH;
        if (excess > 0) {
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    /**
     * Returns the response for the option the user chose for this message.
     *
     * @param sendOption 1 = send, 2 = disregard, 3 = store
     * @return the matching message, or an error message for any other option
     */
    public String sentMessage(int sendOption) {
        if (sendOption == 1) {
            return "Message successfully sent.";
        } else if (sendOption == 2) {
            return "Press 0 to delete the message.";
        } else if (sendOption == 3) {
            return "Message successfully stored.";
        }
        return "Invalid option, please enter 1, 2 or 3.";
    }

    /**
     * Adds this message to the list of messages sent while the program is
     * running.
     */
    public void addToSentMessages() {
        sentMessages.add(this);
    }

    /**
     * Returns the total number of messages sent while the program is running.
     *
     * @return the number of sent messages
     */
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    /**
     * Returns the details of all messages sent while the program is running, in
     * the order: Message ID, Message Hash, Recipient, Message.
     *
     * @return the details of every sent message, or an empty String if none
     * were sent
     */
    public static String printMessages() {
        StringBuilder allMessages = new StringBuilder();
        for (Message sent : sentMessages) {
            allMessages.append("Message ID: ").append(sent.messageID).append("\n");
            allMessages.append("Message Hash: ").append(sent.messageHash).append("\n");
            allMessages.append("Recipient: ").append(sent.recipient).append("\n");
            allMessages.append("Message: ").append(sent.messageText).append("\n\n");
        }
        return allMessages.toString();
    }

    /**
     * Clears the list of sent messages (used to reset the state between unit
     * tests).
     */
    public static void clearSentMessages() {
        sentMessages.clear();
    }

    /**
     * Creates the message hash: first two digits of the ID, the message number,
     * then the first and last words of the message, all in capital letters.
     *
     * @return the message hash, for example 00:0:HITONIGHT
     */
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
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
