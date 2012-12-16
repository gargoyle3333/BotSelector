package net.sim.classes;

import net.sim.interfaces.BotKeyboardListener;
import net.sim.interfaces.BotMouseListener;
import net.sim.interfaces.BotWindowListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GameBoard {
	
	// Listener fields
	private BotMouseListener mMouseListener;
	private BotKeyboardListener mKeyboardListener;
	private BotWindowListener mWindowListener;
	
	// Constants
	private static final int FRAMES_PER_SECOND = 60;
	
	// Fields for mouse
	private boolean leftMouseDown, rightMouseDown;
	private int x,y, currentX, currentY;
	
	// Fields for screen
	private int currentScreenWidth, currentScreenHeight;
//	private DisplayMode small, fullScreen;
	
	public GameBoard(BotMouseListener mouseListener, BotKeyboardListener keyboardListener, BotWindowListener windowListener) throws LWJGLException {
		
//		small = new DisplayMode(800, 600);
//		fullScreen = Display.getDesktopDisplayMode();
		
		Display.setDisplayMode(new DisplayMode(800,600));
		Display.setResizable(true);
		Display.create();
		
		currentScreenWidth = Display.getWidth();
		currentScreenHeight = Display.getHeight();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		mMouseListener = mouseListener;
		mKeyboardListener = keyboardListener;
		mWindowListener = windowListener;
	}
	
//	public void setFullScreen() throws LWJGLException {
//		Display.destroy();
//		Display.setDisplayMode(fullScreen);
//		GL11.glMatrixMode(GL11.GL_PROJECTION);
//		GL11.glOrtho(0, fullScreen.getWidth(), 0, fullScreen.getHeight(), 1, -1);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		Display.create();
//		Display.setFullscreen(true);
//	}
//
//	public void setSmall() throws LWJGLException {
//		Display.destroy();
//		Display.setDisplayMode(small);
//		GL11.glMatrixMode(GL11.GL_PROJECTION);
//		GL11.glOrtho(0, 800, 0, 600, 1, -1);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);
//		Display.create();
//		Display.setFullscreen(false);
//	}
	
	public void update() {
		pollMouse();
		pollKeyboard();
		pollScreen();
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

	private void pollKeyboard() {
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
	}
	
	private void pollScreen() {
		if (currentScreenWidth != Display.getWidth() || currentScreenHeight != Display.getHeight()) {
			currentScreenWidth = Display.getWidth();
			currentScreenHeight = Display.getHeight();
			mWindowListener.windowResized(currentScreenWidth, currentScreenHeight);
			
			GL11.glViewport(0, 0, currentScreenWidth, currentScreenHeight);
	        GL11.glMatrixMode(GL11.GL_PROJECTION);
	        GL11.glLoadIdentity();
//	        GL11.gluPerspective(45.0f, ((float) width / (float) height), 0.1f,
//	                100.0f);
	        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	        GL11.glLoadIdentity();
			
		}
	}
//	private String correctCase(String input, boolean capital) {
//		if (capital)
//			return input.toUpperCase();
//		else
//			return input.toLowerCase();
//	}

}
