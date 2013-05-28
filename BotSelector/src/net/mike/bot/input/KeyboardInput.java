package net.mike.bot.input;

import net.mike.bot.event.Event;
import net.mike.bot.event.GlobalEventHandler;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class KeyboardInput {
	
	Keyboard mKeyboard;
	boolean created = false;
	
	public KeyboardInput() {
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			System.err.println("Error in creating keyboard.");
		}
		
		if (Keyboard.isCreated()) {
			created = true;
		}
	}
	
	public void pollKeyboard() {
		Keyboard.poll();
		// Iterate over keyboard events and handle them 
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean pressed = Keyboard.getEventKeyState();
			switch (key) {
			case Keyboard.KEY_UP:
			case Keyboard.KEY_W:
				if (pressed) GlobalEventHandler.fireEvent(Event.KEYBOARD_UP_PRESSED, null);
				else GlobalEventHandler.fireEvent(Event.KEYBOARD_UP_RELEASED, null);
				break;
			case Keyboard.KEY_DOWN:
			case Keyboard.KEY_S:
				if (pressed) GlobalEventHandler.fireEvent(Event.KEYBOARD_DOWN_PRESSED, null);
				else GlobalEventHandler.fireEvent(Event.KEYBOARD_DOWN_RELEASED, null);
				break;
			case Keyboard.KEY_LEFT:
			case Keyboard.KEY_A:
				if (pressed) GlobalEventHandler.fireEvent(Event.KEYBOARD_LEFT_PRESSED, null);
				else GlobalEventHandler.fireEvent(Event.KEYBOARD_LEFT_RELEASED, null);
				break;
			case Keyboard.KEY_RIGHT:
			case Keyboard.KEY_D:
				if (pressed) GlobalEventHandler.fireEvent(Event.KEYBOARD_RIGHT_PRESSED, null);
				else GlobalEventHandler.fireEvent(Event.KEYBOARD_RIGHT_RELEASED, null);
				break;
			default:
				// Ignore
			}
		}
	}
	
}
