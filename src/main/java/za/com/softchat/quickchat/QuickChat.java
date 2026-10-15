package za.com.softchat.quickchat;

import java.util.Scanner;

/**
 * Console application for the QuickChat registration and login feature.
 *
 * @author Angel Qosha ST10533417
 */
public class QuickChat {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Login login = new Login();

            System.out.println("=== QuickChat Registration ===");

            System.out.print("Enter your first name: ");
            String firstName = input.nextLine();

            System.out.print("Enter your last name: ");
            String lastName = input.nextLine();

            System.out.print("Enter a username: ");
            String username = input.nextLine();
            if (login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
            }

            System.out.print("Enter a password: ");
            String password = input.nextLine();
            if (login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
            }

            System.out.print("Enter your cell phone number: ");
            String cellNumber = input.nextLine();
            if (login.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully added.");
            }

            System.out.println(login.registerUser(firstName, lastName,
                    username, password, cellNumber));

            System.out.println();
            System.out.println("=== QuickChat Login ===");

            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loginSuccess));

            if (loginSuccess) {
                System.out.println();
                System.out.println("Welcome to QuickChat.");
                int numberOfMessages = 0;
                while (numberOfMessages < 1) {
                    System.out.print("How many messages do you wish to enter? ");
                    try {
                        numberOfMessages = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        numberOfMessages = 0;
                    }
                    if (numberOfMessages < 1) {
                        System.out.println("Please enter a whole number of 1 or more.");
                    }
                }
                System.out.println("You can enter " + numberOfMessages + " message(s).");
                System.out.println();
                int messagesEntered = 0;
                String choice = "";
                while (!choice.equals("3")) {
                    System.out.println();
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.print("Choose an option: ");
                    choice = input.nextLine().trim();

                    if (choice.equals("1")) {
                        if (messagesEntered >= numberOfMessages) {
                            System.out.println("You have already entered all " + numberOfMessages + " message(s).");
                        } else {
                            for (int i = messagesEntered; i < numberOfMessages; i++) {
                                System.out.println();
                                System.out.println("--- Message number " + i + " ---");
                                System.out.print("Enter the recipient's cell number: ");
                                String recipient = input.nextLine().trim();
                                System.out.print("Enter your message: ");
                                String messageText = input.nextLine();
                                messagesEntered++;
                            }
                        }
                    } else if (choice.equals("2")) {
                        System.out.println("Coming Soon.");
                    } else if (choice.equals("3")) {
                        System.out.println("Goodbye.");
                    } else {
                        System.out.println("Invalid option, please enter 1, 2 or 3.");
                    }
                }
            }
        }
    }
}
