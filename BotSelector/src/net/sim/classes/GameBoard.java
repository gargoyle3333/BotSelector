package net.sim.classes;

import net.sim.interfaces.BotKeyboardListener;
import net.sim.interfaces.BotMouseListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GameBoard {
	
	private BotMouseListener mMouseListener;
	private BotKeyboardListener mKeyboardListener;
//	private DisplayMode small, fullScreen;
	
	// Constants
	private static final int FRAMES_PER_SECOND = 60;
	
	// Fields for mouse
	private boolean leftMouseDown, rightMouseDown;
	private int x,y, currentX, currentY;
	
	public GameBoard(BotMouseListener mouseListener, BotKeyboardListener keyboardListener) throws LWJGLException {
		
//		small = new DisplayMode(800, 600);
//		fullScreen = new DisplayMode(1280,1024);
		
		Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
		Display.create();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		mMouseListener = mouseListener;
		mKeyboardListener = keyboardListener;
	}
	
//	public void setFullScreen() throws LWJGLException {
//		Display.setDisplayMode(fullScreen);
//		Display.setFullscreen(true);
//	}
//
//	public void setSmall() throws LWJGLException {
//		Display.setDisplayMode(small);
//		Display.setFullscreen(false);
//	}
	
	public void update() {
		pollMouse();
		pollKeyboard();
		Display.update();
		Display.sync(FRAMES_PER_SECOND);
	}
	
	private void pollMouse() {
		if (((currentX = Mouse.getX()) != x)
				|| ((currentY = Mouse.getY()) != y)) {
			x = currentX;
			y = currentY;
			if (leftMouseDown) mMouseListener.leftDragged(x, y);
			if (rightMouseDown) mMouseListener.rightDragged(x, y);
		}
		if (Mouse.isButtonDown(0) ^ leftMouseDown) {
			leftMouseDown = Mouse.isButtonDown(0);
			if (leftMouseDown) {
				mMouseListener.leftButtonClicked(x, y);
			} else {
				mMouseListener.leftButtonReleased(x, y);
			}
		}
		if (Mouse.isButtonDown(1) ^ rightMouseDown) {
			rightMouseDown = Mouse.isButtonDown(1);
			if (rightMouseDown) {
				mMouseListener.rightButtonClicked(x, y);
			} else {
				mMouseListener.rightButtonReleased(x, y);
			}
		}
	}

	private boolean pollKeyboard() {
		int key;
		while (Keyboard.next()) {
			key = Keyboard.getEventKey();
			if (Keyboard.isKeyDown(key)) {
				mKeyboardListener.keyPressed(key);
			} else {
				mKeyboardListener.keyReleased(key);
				mKeyboardListener.keyTyped(key);
			}
		}
		return false;
	}

//	private String correctCase(String input, boolean capital) {
//		if (capital)
//			return input.toUpperCase();
//		else
//			return input.toLowerCase();
//	}

}
