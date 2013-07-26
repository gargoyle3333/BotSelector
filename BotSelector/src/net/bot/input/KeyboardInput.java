package net.bot.input;

import net.bot.event.handler.KeyboardEventHandler;

import org.lwjgl.input.Keyboard;

public class KeyboardInput {
	
	public void pollKeyboard() {
		// Iterate over keyboard events and handle them 
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			if (Keyboard.getEventKeyState()) {
				KeyboardEventHandler.keyPressed(key);
			} else {
				KeyboardEventHandler.keyReleased(key);
			}
			
		}
	}
	
}
