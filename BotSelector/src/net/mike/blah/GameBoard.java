package net.mike.blah;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GameBoard {
	
	// Fields for mouse
	private boolean leftMouseDown, rightMouseDown;
	private int x,y, currentX, currentY;
	
	//Fields for keyboard
	private boolean shiftHeldDown = false;
	
	public void start() {
		try {
			Display.setDisplayModeAndFullscreen(new DisplayMode(800,600));
			Display.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (!Display.isCloseRequested()) {
			pollMouse();
			pollKeyboard();
			Display.update();
		}
		
	}
	
	private void pollMouse() {
			if (((currentX = Mouse.getX()) != x) || ((currentY = Mouse.getY()) != y)) {
				x = currentX;
				y = currentY;
				if (leftMouseDown || rightMouseDown) {
					System.out.printf("Mouse dragged to new location (%d,%d)\n",x,y);
					// TODO handle event MouseDragged
				}
			}
			if (Mouse.isButtonDown(0) ^ leftMouseDown) {
				leftMouseDown = Mouse.isButtonDown(0);
				if (leftMouseDown) {
					// TODO handle event left button clicked
					System.out.printf("Left mouse button clicked at %d,%d)\n",x,y);
				} else {
					// TODO handle event left button released
					System.out.printf("Left mouse button released at %d,%d)\n",x,y);
				}
			}
			if (Mouse.isButtonDown(1) ^ rightMouseDown) {
				rightMouseDown = Mouse.isButtonDown(1);
				if (rightMouseDown) {
					// TODO handle event right button clicked
					System.out.printf("Right mouse button clicked at %d,%d)\n",x,y);
				} else {
					// TODO handle event right button released
					System.out.printf("Right mouse button released %d,%d)\n",x,y);
				}
			}
	}

	private boolean pollKeyboard() {
		int key;
		while (Keyboard.next()) {
			key = Keyboard.getEventKey();
			if ((key == Keyboard.KEY_LSHIFT) || (key == Keyboard.KEY_RSHIFT)) {
				shiftHeldDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
			}
			if (key == Keyboard.KEY_CAPITAL) shiftHeldDown = Keyboard.isKeyDown(Keyboard.KEY_CAPITAL);
			if (Keyboard.isKeyDown(key)) {
				System.out.printf("Key pressed: %s\n", correctCase(Keyboard.getKeyName(key), shiftHeldDown));
			} else {
				System.out.printf("Key released: %s\n", correctCase(Keyboard.getKeyName(key), shiftHeldDown));
			}
		}
		return false;
	}
	
	private String correctCase(String input, boolean capital) {
		if (capital) return input.toUpperCase();
		else return input.toLowerCase();
	}
	
	public static void main(String[] args) {
		GameBoard gameboard = new GameBoard();
		gameboard.start();
	}

}
