/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package prog5121_p1;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    // Reuse the same Login object for test methods
    private Login login = new Login();
    @Test
    public void testUsernameCorrect() {
        assertTrue(login.checkUserName("kyl_1")); // Valid username with underscore, <=5 chars
    }
    @Test
    public void testUsernameIncorrect() {
        assertFalse(login.checkUserName("kyle!!!!!!!")); // Invalid due to no underscore and too long
    }
    @Test
    public void testPasswordCorrect() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!")); // Meets complexity
    }
    @Test
    public void testPasswordIncorrect() {
        assertFalse(login.checkPasswordComplexity("password")); // No capitals, no special char, no digit
    }
    @Test
    public void testCellPhoneCorrect() {
        assertTrue(login.checkCellPhoneNumber("+27838968976")); // Valid SA cellphone with country code
    }
    @Test
    public void testCellPhoneIncorrect() {
        assertFalse(login.checkCellPhoneNumber("08966553")); // Invalid - missing +27 and length
    }
    @Test
    public void testRegistrationSuccessful() {
        String result = login.registerUser("John", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Registration successful.", result);
    }
    @Test
    public void testRegistrationInvalidUsername() {
        String result = login.registerUser("John", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }
    @Test
    public void testRegistrationInvalidPassword() {
        String result = login.registerUser("John", "Smith", "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }
    @Test
    public void testRegistrationInvalidCellPhone() {
        String result = login.registerUser("John", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", result);
    }
    @Test
    public void testLoginSuccessful() {
        login.registerUser("John", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }
    @Test
    public void testLoginFailed() {
        login.registerUser("John", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong", "wrong"));
    }
    @Test
    public void testSuccessfulLoginMessage() {
        login.registerUser("John", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome John, Smith it is great to see you again.", login.returnLoginStatus(true));
    }
    @Test
    public void testFailedLoginMessage() {
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }
}
