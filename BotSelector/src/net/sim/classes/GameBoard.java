package net.sim.classes;

import net.sim.interfaces.BotKeyboardListener;
import net.sim.interfaces.BotMouseListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

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
		
		//Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
		
		final double maxHeight = 300;
		final double depth = 800;
		final double scale = (depth - maxHeight) / depth;
		

		Display.setDisplayMode(new DisplayMode(800, 600));
		Display.create();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(scale * -400, scale * 400, scale * -300, scale * 300, depth - maxHeight, depth + maxHeight);
		GL11.glTranslated(-400, -300, -depth);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		
		GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		
		//Allocate light buffer
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, Utils.fBuffer4(Color.GREY));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Utils.fBuffer4(Color.WHITE));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, Utils.fBuffer4(Color.BLACK));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Utils.fBuffer4(0, 0, 1, 0));
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_NORMALIZE);
		
		
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
