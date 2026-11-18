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

    private static String storageFile = "storedMessages.json";
    private static final int MAX_MESSAGE_LENGTH = 250;
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();
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
     * Adds this message to the list of disregarded messages.
     */
    public void addToDisregardedMessages() {
        disregardedMessages.add(this);
    }

    /**
     * Adds this message to the list of stored messages.
     */
    public void addToStoredMessages() {
        storedMessages.add(this);
    }

    /**
     * Records this message's ID and hash in the message ID and message hash
     * lists.
     */
    public void recordMessageIDAndHash() {
        messageIDs.add(messageID);
        messageHashes.add(messageHash);
    }

    /**
     * Clears every message list (used to reset the state between unit tests).
     */
    public static void clearAllMessages() {

        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    /**
     * Reads the messages saved in the JSON file into the stored messages array.
     * Adapted from the Gson User Guide: Google, n.d. Gson User Guide. Available
     * at: https://github.com/google/gson/blob/main/UserGuide.md [Accessed 19
     * October 2026].
     *
     * @return true if the file was read (or does not exist yet), false if it
     * could not be read
     */
    public static boolean loadStoredMessages() {
        storedMessages.clear();
        File jsonFile = new File(storageFile);
        if (!jsonFile.exists()) {
            return true;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(jsonFile)) {
            Type listType = new TypeToken<List<Message>>() {
            }.getType();
            List<Message> messagesInFile = gson.fromJson(reader, listType);
            if (messagesInFile != null) {
                storedMessages.addAll(messagesInFile);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Returns a copy of the stored messages array.
     *
     * @return the stored messages
     */
    public static List<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }

    /**
     * Returns a copy of the sent messages array.
     *
     * @return the sent messages
     */
    public static List<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    /**
     * Returns a copy of the disregarded messages array.
     *
     * @return the disregarded messages
     */
    public static List<Message> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }

    /**
     * Sets the JSON file used to store messages, so unit tests can use their
     * own file instead of the real storedMessages.json.
     *
     * @param fileName the name of the JSON file to use
     */
    public static void setStorageFile(String fileName) {
        storageFile = fileName;
    }

    /**
     * Returns the sender and recipient of every stored message.
     *
     * @param senderName the name of the logged-in user who stored the messages
     * @return one line per stored message, or a notice if there are none
     */
    public static String displaySenderAndRecipient(String senderName) {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder result = new StringBuilder();
        for (Message stored : storedMessages) {
            result.append("Sender: ").append(senderName)
                    .append(" | Recipient: ").append(stored.recipient).append("\n");
        }
        return result.toString();
    }

    /**
     * Finds the longest message in the stored messages array.
     *
     * @return the longest stored message, or a notice if there are none
     */
    public static String displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        Message longest = storedMessages.get(0);
        for (Message stored : storedMessages) {
            if (stored.messageText.length() > longest.messageText.length()) {
                longest = stored;
            }
        }
        return longest.messageText;
    }

    /**
     * Combines the sent and stored messages so they can be searched together.
     *
     * @return every sent and stored message
     */
    private static List<Message> getSentAndStoredMessages() {
        List<Message> allMessages = new ArrayList<>(sentMessages);
        allMessages.addAll(storedMessages);
        return allMessages;
    }

    /**
     * Searches the sent and stored messages for a message ID.
     *
     * @param searchID the message ID to look for
     * @return the recipient and message for that ID, or a notice if it is not
     * found
     */
    public static String searchByMessageID(String searchID) {
        for (Message found : getSentAndStoredMessages()) {
            if (found.messageID.equals(searchID)) {
                return "Recipient: " + found.recipient + " | Message: " + found.messageText;
            }
        }
        return "Message ID not found.";
    }

    /**
     * Searches the sent and stored messages for every message sent to a
     * recipient.
     *
     * @param searchRecipient the recipient's cell number
     * @return every matching message, or a notice if there are none
     */
    public static String searchByRecipient(String searchRecipient) {
        StringBuilder result = new StringBuilder();
        for (Message found : getSentAndStoredMessages()) {
            if (found.recipient.equals(searchRecipient)) {
                result.append(found.messageText).append("\n");
            }
        }
        if (result.length() == 0) {
            return "No stored messages found for that recipient.";
        }
        return result.toString();
    }

    /**
     * Writes the stored messages array back to the JSON file.
     *
     * @return true if the file was written, false if it could not be written
     */
    private static boolean saveStoredMessages() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(storageFile)) {
            gson.toJson(storedMessages, writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Deletes the first stored message that has the given message hash, from
     * the stored messages array and from the JSON file.
     *
     * @param searchHash the message hash of the message to delete
     * @return a confirmation that the message was deleted, or a notice if not
     * found
     */
    public static String deleteByMessageHash(String searchHash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message stored = storedMessages.get(i);
            if (stored.messageHash.equalsIgnoreCase(searchHash)) {
                storedMessages.remove(i);
                if (!saveStoredMessages()) {
                    return "The message could not be deleted from the file.";
                }
                return "Message: \"" + stored.messageText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
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
        File storageFile = new File(storageFile);

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
