package core;

import javafx.scene.input.KeyCode;

public class KeyHelper {
	
	public static KeyCode reverseKey(KeyCode lastKey) throws IllegalArgumentException {
		KeyCode oppositeKey;
		
		switch (lastKey) {
		case W:
			oppositeKey = KeyCode.S;
			break;
		case A:
			oppositeKey = KeyCode.D;
			break;
		case S:
			oppositeKey = KeyCode.W;
			break;
		case D:
			oppositeKey = KeyCode.A;
			break;
		default:
			throw new IllegalArgumentException(lastKey + ": Last key was not a valid key");
		}
		
		
		System.out.println("Returning " + oppositeKey);
		return oppositeKey;
	}
	
	public static boolean validKey(KeyCode keyPressed) {
		boolean valid;
		
		switch (keyPressed) {
		case W:
			valid = true;
			break;
		case A:
			valid = true;
			break;
		case S:
			valid = true;
			break;
		case D:
			valid = true;
			break;
		default:
			valid = false;
		}
		
		return valid;
	}

}
