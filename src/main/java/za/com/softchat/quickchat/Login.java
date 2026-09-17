/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.com.softchat.quickchat;

/**
 *
 * @author Angel
 */
public class Login {
private String registeredUsername;
private String registeredPassword;
private String firstName;
private String lastName;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char currentCharacter = password.charAt(i);

            if (Character.isUpperCase(currentCharacter)) {
                hasCapital = true;
            } else if (Character.isDigit(currentCharacter)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(currentCharacter)) {
                hasSpecial = true;
            }
        }

        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Validates a South African cell phone number.
     *
     * Regular expression syntax adapted from: Oracle, 2023. Class Pattern.
     * [Online] Available at:
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     * [Accessed 17 September 2026].
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+27[0-9]{9}$");
    }
    public String registerUser(String firstName, String lastName,
        String username, String password, String cellNumber) {

    if (!checkUserName(username)) {
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    if (!checkPasswordComplexity(password)) {
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    if (!checkCellPhoneNumber(cellNumber)) {
        return "Cell phone number incorrectly formatted or does not contain international code.";
    }

    this.firstName = firstName;
    this.lastName = lastName;
    this.registeredUsername = username;
    this.registeredPassword = password;

    return "User registered successfully.";
}
}
