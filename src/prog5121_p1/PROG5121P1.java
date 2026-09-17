/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prog5121_p1;

import java.util.Scanner;

/**
 *
 * @author ralik
 */
public class PROG5121P1 {
    
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        
        // Create Login object to handle registration and login logic
        Login userAccount = new Login();
        
        String firstName;
        String lastName;
        String username;
        String password;
        String cellphone;
        
        System.out.println("=================================");
        System.out.println("              REGISTRATION");
        System.out.println("=================================");
        
        // Collect user's first name
        System.out.print("Enter first name: ");
        firstName = input.nextLine();
        
        // Collect user's last name
        System.out.print("Enter last name: ");
        lastName = input.nextLine();
        
        // Repeatedly prompt for valid username until validation passes
        do {
            System.out.print("Enter username (must contain '_' and max 5 chars): ");
            username = input.nextLine();
            
            if (userAccount.checkUserName(username)) {
                System.out.println("Username successfully captured.");
            } else {
                System.out.println("Username is not correctly formatted; "
                         + "Please ensure that your username contains an underscore "
                         + "and is no more than five characters in length.");
            }
            
        } while (!userAccount.checkUserName(username));
        
        // Repeatedly prompt for valid password until validation passes
        do {
            System.out.print("Enter password (min 8 chars, capital letter, number, special char): ");
            password = input.nextLine();
            
            if (userAccount.checkPasswordComplexity(password)) {
               System.out.println("Password successfully captured.");
            } else {
                System.out.println("Password is not correctly formatted; "
                        + "please ensure it contains at least eight characters, "
                        + "a capital letter, a number, and a special character.");
                
            }
            
        } while (!userAccount.checkPasswordComplexity(password));
        
        // Repeatedly prompt for valid cellphone number until validation passes
        do {
            System.out.print("Enter South African cell phone number (with +27): ");
            cellphone = input.nextLine();
            
            if (userAccount.checkCellPhoneNumber(cellphone)) {
                System.out.println("Cell phone number successfully added. ");
            } else {
                System.out.println("Cell phone number incorrectly formatted "
                        + "or does not contain international code.");
                
            }
            
        } while (!userAccount.checkCellPhoneNumber(cellphone));
        
        // Register user, store details, and get registration message
        String registrationMessage = userAccount.registerUser(
                firstName,
                lastName,
                username,
                password,
                cellphone
        );
        
        System.out.println(registrationMessage);
        
        // Continue to login if registration succeeded
        if (userAccount.isRegistered()) {
            
            System.out.println();
            System.out.println("=================================");
            System.out.println("                  LOGIN");
            
            
            // Allow the user to keep trying until the correct login details are entered
            boolean isLoginSuccessful = false;
            
            while (!isLoginSuccessful) {
                
            
            System.out.print("Enter username: ");
            String loginUsername = input.nextLine();
            
            System.out.print("Enter password: ");
            String loginPassword = input.nextLine();
            
            // Check whether the entered username and password are correct
            isLoginSuccessful = userAccount.loginUser(loginUsername, loginPassword);
            
            // Display the login result 
            System.out.println(userAccount.returnLoginStatus(isLoginSuccessful));
            
            // Ask the user to try again if the login details are incorrect
            if (!isLoginSuccessful) {
                System.out.println("Please re-enter your username and password.");
                System.out.println();
            }
        }
        }
        
        input.close();
    }
    
}
