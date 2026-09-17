/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prog5121_p1;

/**
 *
 * @author ralik
 */
public class Login {
    
    // Stored user details after registration
    private String savedUsername;
    private String savedPassword;
    private String firstName;
    private String lastName;
    private String savedCellphone;
    
    
    public boolean checkUserName(String username) {
        return username.length() <=5 && username.contains("_");
    }
    
    
    public boolean checkPasswordComplexity(String password) {
        
        if (password.length() < 8) {
            return false;
        }
        
        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        
        // Check each char of the password
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapitalLetter = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        
        return hasCapitalLetter && hasNumber && hasSpecialChar;
    }
    
    
    public boolean checkCellPhoneNumber(String cellphone) {
        return cellphone.matches("^\\+27[0-9]{9}$");
        
    }
    
    public String registerUser(String firstName, String lastName, String username, String password, String cellphone) {
        
        // Validate username
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    
        // Validate password
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character."; 
        }
        
        // validate cellphone number
        if (!checkCellPhoneNumber(cellphone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        // Save details for login checks
        this.firstName = firstName;
        this.lastName = lastName;
        this.savedUsername = username;
        this.savedPassword = password;
        this.savedCellphone = cellphone;
        
        return "Registration successful.";
}
    
    public boolean loginUser(String username, String password) {
        return username.equals(savedUsername) && password.equals(savedPassword);
    }
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again." ;
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
    public boolean isRegistered() {
        return savedUsername != null && savedPassword != null && savedCellphone != null;
    }
}
