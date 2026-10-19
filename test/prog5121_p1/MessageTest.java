// Commit 6: Unit tests
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package prog5121_p1;

import java.io.File;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Message class, using the test data from the Part 2 brief.
 */
public class MessageTest {

    // Test data - message 1
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";

    // Test data - message 2
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    private Message message1;
    private Message message2;

    @Before
    public void setUp() {
        message1 = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        message2 = new Message("0087654321", 1, RECIPIENT_2, TEXT_2);
    }

    @After
    public void cleanUp() {
        // Remove the JSON file created by the store test
        new File("storedMessages.json").delete();
    }

    // ---------- Message length ----------
    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(TEXT_1));
    }

    @Test
    public void testMessageLengthFailure() {
        String tooLong = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(tooLong));
    }

    // ---------- Recipient number ----------
    @Test
    public void testRecipientNumberSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    // ---------- Message hash ----------
    @Test
    public void testMessageHashForTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        // Each row: ID, message number, text, expected hash
        String[] ids = {"0012345678", "0087654321", "1299999999"};
        String[] texts = {TEXT_1, TEXT_2, "Hello   there,  see you soon!"};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT", "12:2:HELLOSOON"};

        for (int i = 0; i < ids.length; i++) {
            Message test = new Message(ids[i], i, RECIPIENT_1, texts[i]);
            assertEquals(expected[i], test.createMessageHash());
        }
    }

    // ---------- Message ID ----------
    @Test
    public void testMessageIDCreated() {
        String id = Message.generateMessageID();
        System.out.println("Message ID generated: " + id);

        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
        assertTrue(new Message(id, 0, RECIPIENT_1, TEXT_1).checkMessageID());
    }

    @Test
    public void testMessageIDTooLong() {
        assertFalse(new Message("12345678901", 0, RECIPIENT_1, TEXT_1).checkMessageID());
    }

    // ---------- Send / Disregard / Store (choice is typed into the Scanner) ----------
    @Test
    public void testSendMessage() {
        assertEquals("Message successfully sent.", message1.SentMessage(new Scanner("1\n")));
    }

    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", message2.SentMessage(new Scanner("2\n")));
    }

    @Test
    public void testStoreMessage() {
        assertEquals("Message successfully stored.", message1.SentMessage(new Scanner("3\n")));
        assertTrue(new File("storedMessages.json").exists());
    }

    @Test
    public void testInvalidOptionAsksAgain() {
        // "9" is invalid, so the method asks again and then accepts "1"
        assertEquals("Message successfully sent.", message1.SentMessage(new Scanner("9\n1\n")));
    }

    // ---------- Totals and printing ----------
    @Test
    public void testReturnTotalMessages() {
        int totalBefore = Message.returnTotalMessages();

        message1.SentMessage(new Scanner("1\n"));   // message 1 is sent
        message2.SentMessage(new Scanner("2\n"));   // message 2 is disregarded, so not counted

        assertEquals(totalBefore + 1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessagesShowsSentMessages() {
        message1.SentMessage(new Scanner("1\n"));
        String printed = Message.printMessages();

        assertTrue(printed.contains("Message ID: 0012345678"));
        assertTrue(printed.contains("Message Hash: 00:0:HITONIGHT"));
        assertTrue(printed.contains("Recipient: +27718693002"));
        assertTrue(printed.contains(TEXT_1));
    }
}