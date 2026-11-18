package za.com.softchat.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Message class.
 *
 * @author Angel Qosha ST10533417
 */
public class MessageTest {

    /**
     * Clears the sent messages before every test so the tests do not affect
     * each other.
     */
    private static final String TEST_FILE = "testStoredMessages.json";

    /**
     * Clears all message lists and uses a separate JSON file before every test.
     */
    @BeforeEach
    public void resetMessages() {
        Message.clearAllMessages();
        Message.setStorageFile(TEST_FILE);
    }

    /**
     * Deletes the test JSON file after every test.
     */
    @AfterEach
    public void deleteTestFile() {
        new File(TEST_FILE).delete();
    }

    private Message testMessage1() {
        return new Message("0000000001", 0, "+27834557896", "Did you get the cake?");
    }

    private Message testMessage2() {
        return new Message("0000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
    }

    private Message testMessage3() {
        return new Message("0000000003", 2, "+27834484567", "Yohoooo, I am at your gate.");
    }

    private Message testMessage4() {
        return new Message("0000000004", 3, "0838884567", "It is dinner time!");
    }

    private Message testMessage5() {
        return new Message("0000000005", 4, "+27838884567", "Ok, I am leaving without you.");
    }

    @Test
    public void testMessageHashTestCase1() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        String[] messageTexts = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };
        String[] expectedHashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        for (int i = 0; i < messageTexts.length; i++) {
            Message message = new Message("0012345678", i, "+27718693002", messageTexts[i]);
            assertEquals(expectedHashes[i], message.createMessageHash());
        }
    }

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.",
                Message.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(longMessage));
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        Message message = new Message("0012345678", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell());
    }

    @Test
    public void testMessageSentOptionSend() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    public void testMessageSentOptionDisregard() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    public void testMessageSentOptionStore() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    public void testMessageIDWithinTenCharacters() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageIDLongerThanTenCharacters() {
        Message message = new Message("00123456789", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertFalse(message.checkMessageID());
    }

    @Test
    public void testGeneratedMessageIDIsCreated() {
        String messageID = Message.generateMessageID();
        assertEquals(10, messageID.length());
        assertTrue(messageID.matches("\\d{10}"));
        assertEquals("Message ID generated: " + messageID,
                "Message ID generated: " + messageID);
    }

    @Test
    public void testGeneratedMessageIDsAreDifferent() {
        assertNotEquals(Message.generateMessageID(), Message.generateMessageID());
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        testMessage1().addToSentMessages();
        testMessage4().addToSentMessages();

        assertEquals(2, Message.getSentMessages().size());
        assertEquals("Did you get the cake?", Message.getSentMessages().get(0).getMessageText());
        assertEquals("It is dinner time!", Message.getSentMessages().get(1).getMessageText());
    }

    @Test
    public void testDisregardedMessagesArrayCorrectlyPopulated() {
        testMessage3().addToDisregardedMessages();

        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals("Yohoooo, I am at your gate.",
                Message.getDisregardedMessages().get(0).getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() {
        testMessage2().addToStoredMessages();
        testMessage5().addToStoredMessages();

        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.displayLongestMessage());
    }
}
