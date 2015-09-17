package csc478b;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import fileIO.DecryptFile;
import fileIO.EncryptFile;

public class DecryptTests {

	@Test
	public final void testDecrypt() {
		String fileName = "plainfile";
		File sourceFile = new File(fileName + ".txt");
		File encryptedFile = new File(sourceFile.getPath() + ".des");
		File decryptedFile = new File(encryptedFile.getPath() + "_decrypted.txt");
		
		if (!sourceFile.exists()) {
			fail("Test file not present");
		}
		try {
			EncryptFile.encrypt(fileName + ".txt");
			assertTrue(encryptedFile.exists());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Encryption failed with error " + e.getLocalizedMessage());
		}
		try {
			DecryptFile.decrypt(encryptedFile.getPath());
			assertTrue(decryptedFile.exists());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Decryption failed with error: " + e.getLocalizedMessage());
		}
		System.out.println("All tests passed");
	}

}
