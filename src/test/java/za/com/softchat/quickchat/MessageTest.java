package za.com.softchat.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Message class.
 *
 * @author Angel Qosha ST10533417
 */
public class MessageTest {

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
}
