package za.com.softchat.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login class.
 * @author Angel Qosha ST10533417
 */
public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testRegisterUserSuccessful() {
        Login login = new Login();
        assertEquals("User registered successfully.",
                login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testRegisterUserBadUsername() {
        Login login = new Login();
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.registerUser("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void testRegisterUserBadPassword() {
        Login login = new Login();
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser("Kyle", "Smith", "kyl_1", "password", "+27838968976"));
    }

    @Test
    public void testRegisterUserBadCellNumber() {
        Login login = new Login();
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.",
                login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553"));
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong_1", "wrongpassword"));
    }

    @Test
    public void testLoginStatusSuccessMessage() {
        Login login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle, Smith it is great to see you.",
                login.returnLoginStatus(true));
    }

    @Test
    public void testLoginStatusFailureMessage() {
        Login login = new Login();
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }
}