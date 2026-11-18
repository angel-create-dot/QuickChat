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
                    System.out.println("4) Stored Messages");
                    System.out.print("Choose an option: ");
                    choice = input.nextLine().trim();

                    if (choice.equals("1")) {
                        if (messagesEntered >= numberOfMessages) {
                            System.out.println("You have already entered all " + numberOfMessages + " message(s).");
                        } else {
                            for (int i = messagesEntered; i < numberOfMessages; i++) {
                                System.out.println();
                                System.out.println("--- Message number " + i + " ---");
                                String recipient = "";
                                boolean validRecipient = false;
                                while (!validRecipient) {
                                    System.out.print("Enter the recipient's cell number: ");
                                    recipient = input.nextLine().trim();
                                    validRecipient = login.checkCellPhoneNumber(recipient);
                                    if (validRecipient) {
                                        System.out.println("Cell phone number successfully captured.");
                                    } else {
                                        System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
                                    }
                                }
                                String messageText = "";
                                boolean validMessage = false;
                                while (!validMessage) {
                                    System.out.print("Enter your message: ");
                                    messageText = input.nextLine();
                                    if (messageText.trim().isEmpty()) {
                                        System.out.println("Message cannot be empty.");
                                    } else {
                                        String lengthResult = Message.checkMessageLength(messageText);
                                        System.out.println(lengthResult);
                                        validMessage = lengthResult.equals("Message ready to send.");
                                    }
                                }
                                String messageID = Message.generateMessageID();
                                Message message = new Message(messageID, i, recipient, messageText);
                                String messageHash = message.getMessageHash();
                                message.recordMessageIDAndHash();
                                if (message.checkMessageID()) {
                                    System.out.println("Message ID generated: " + message.getMessageID());
                                }
                                System.out.println("Message Hash: " + messageHash);
                                String sendChoice = "";
                                while (!sendChoice.equals("1") && !sendChoice.equals("2")
                                        && !sendChoice.equals("3")) {
                                    System.out.println("1) Send Message");
                                    System.out.println("2) Disregard Message");
                                    System.out.println("3) Store Message to send later");
                                    System.out.print("Choose an option: ");
                                    sendChoice = input.nextLine().trim();

                                    if (sendChoice.equals("1")) {
                                        message.addToSentMessages();
                                        System.out.println(message.sentMessage(1));
                                        System.out.println("Message ID: " + message.getMessageID());
                                        System.out.println("Message Hash: " + message.getMessageHash());
                                        System.out.println("Recipient: " + message.getRecipient());
                                        System.out.println("Message: " + message.getMessageText());
                                    } else if (sendChoice.equals("2")) {
                                        String deleteKey = "";
                                        while (!deleteKey.equals("0")) {
                                            System.out.println("Press 0 to delete the message.");
                                            deleteKey = input.nextLine().trim();
                                        }
                                        message.addToDisregardedMessages();
                                    } else if (sendChoice.equals("3")) {
                                        if (message.storeMessage()) {
                                            System.out.println("Message successfully stored.");
                                        } else {
                                            System.out.println("Message could not be stored.");
                                        }
                                    } else {
                                        System.out.println("Invalid option, please enter 1, 2 or 3.");
                                    }
                                }
                                messagesEntered++;
                            }
                            System.out.println();
                            System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
                        }
                    } else if (choice.equals("2")) {
                        System.out.println("Coming Soon.");
                    } else if (choice.equals("3")) {
                        System.out.println("Goodbye.");
                    } else if (choice.equals("4")) {
                        if (!Message.loadStoredMessages()) {
                            System.out.println("Stored messages could not be read.");
                        } else {
                            String storedChoice = "";
                            while (!storedChoice.equals("7")) {
                                System.out.println();
                                System.out.println("--- Stored Messages ---");
                                System.out.println("1) Display sender and recipient of all stored messages");
                                System.out.println("2) Display the longest stored message");
                                System.out.println("3) Search for a message ID");
                                System.out.println("4) Search for all messages stored for a recipient");
                                System.out.println("5) Delete a message using the message hash");
                                System.out.println("6) Display a report of all stored messages");
                                System.out.println("7) Back to the main menu");
                                System.out.print("Choose an option: ");
                                storedChoice = input.nextLine().trim();

                                if (storedChoice.equals("1")) {
                                    System.out.println(Message.displaySenderAndRecipient(firstName + " " + lastName));
                                } else if (storedChoice.equals("2")) {
                                    System.out.println("Longest stored message: " + Message.displayLongestMessage());
                                } else if (storedChoice.equals("3")) {
                                    System.out.print("Enter the message ID to search for: ");
                                    String searchID = input.nextLine().trim();
                                    System.out.println(Message.searchByMessageID(searchID));
                                } else if (storedChoice.equals("7")) {
                                    System.out.println("Returning to the main menu.");
                                } else {
                                    System.out.println("Invalid option, please enter 1 to 7.");
                                }
                            }
                        }
                    } else {
                        System.out.println("Invalid option, please enter 1, 2, 3 or 4.");
                    }
                }
            }
        }
    }
}
