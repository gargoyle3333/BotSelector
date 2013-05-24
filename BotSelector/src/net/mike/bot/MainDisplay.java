package net.mike.bot;

import net.mike.bot.event.Event;
import net.mike.bot.event.GlobalEventHandler;
import net.mike.bot.event.IEventHandler;
import net.mike.bot.input.KeyboardInput;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainDisplay implements IEventHandler {
	
	private static final int FRAMES_PER_SECOND = 60;
	
	private static final String GAME_TITLE = "Bot Simulation";
	
	private static final int SCREEN_PAN = 5;

	public static final int BOARD_WIDTH = 1600;
	public static final int BOARD_HEIGHT = 1200;
	
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	private int xViewOffset = 0, yViewOffset = 0;
	private boolean[] arrowKeysPressed;
	
	// Input classes
	private KeyboardInput mKeyboard;
	
	public MainDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
			Display.setTitle(GAME_TITLE);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Set up OpenGL here
		
		// Set up viewpoint
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		// x: 0 to WIDTH; y: 0 to HEIGHT; z: 1 to -1;
		GL11.glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		
		// Set up bot controller
		new SimController();
		
		// Set up input events
		mKeyboard = new KeyboardInput();
		arrowKeysPressed = new boolean[4];
		
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_UP_PRESSED);
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_DOWN_PRESSED);
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_LEFT_PRESSED);
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_RIGHT_PRESSED);
		
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_UP_RELEASED);
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_DOWN_RELEASED);
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_LEFT_RELEASED);
		GlobalEventHandler.subscribeEvent(this, Event.KEYBOARD_RIGHT_RELEASED);
		
	}
	
	public void run() {
		
		while (!Display.isCloseRequested()) {
			
			// Poll inputs
			mKeyboard.pollKeyboard();
			moveScreen(arrowKeysPressed);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.2F, 0.2F, 0.2F, 0F);
			
			// Set the viewpoint
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(xViewOffset, xViewOffset + SCREEN_WIDTH, yViewOffset, yViewOffset + SCREEN_HEIGHT, 1, -1);
			
			// Update
			GlobalEventHandler.fireEvent(Event.UPDATE_ENTITIES, null);
			// Redraw
			GlobalEventHandler.fireEvent(Event.DRAW_ENTITIES, null);
			
			Display.update();
			Display.sync(FRAMES_PER_SECOND); 
		}
		
		// Cleanup
		Display.destroy();
		
	}
	
	private void moveScreen(boolean[] directions) {
		if (directions[0]) {
			yViewOffset += SCREEN_PAN;
			if (yViewOffset > BOARD_HEIGHT - SCREEN_HEIGHT) yViewOffset = BOARD_HEIGHT - SCREEN_HEIGHT;
		}
		if (directions[1]) {
			yViewOffset -= SCREEN_PAN;
			if (yViewOffset < 0) yViewOffset = 0;
		}
		if (directions[2]) {
			xViewOffset -= SCREEN_PAN;
			if (xViewOffset < 0) xViewOffset = 0;
		}
		if (directions[3]) {
			xViewOffset += SCREEN_PAN;
			if (xViewOffset > BOARD_WIDTH - SCREEN_WIDTH) xViewOffset = BOARD_WIDTH - SCREEN_WIDTH;
		}
	}
	
	@Override
	public void handleEvent(Event event, Object info) {
		switch (event) {
		case KEYBOARD_UP_PRESSED: 
			arrowKeysPressed[0] = true;
			break;
		case KEYBOARD_DOWN_PRESSED: 
			arrowKeysPressed[1] = true;
			break;
		case KEYBOARD_LEFT_PRESSED: 
			arrowKeysPressed[2] = true;
			break;
		case KEYBOARD_RIGHT_PRESSED: 
			arrowKeysPressed[3] = true;
			break;
			
		case KEYBOARD_UP_RELEASED: 
			arrowKeysPressed[0] = false;
			break;
		case KEYBOARD_DOWN_RELEASED: 
			arrowKeysPressed[1] = false;
			break;
		case KEYBOARD_LEFT_RELEASED: 
			arrowKeysPressed[2] = false;
			break;
		case KEYBOARD_RIGHT_RELEASED: 
			arrowKeysPressed[3] = false;
			break;
		default:
			System.err.println("Unrecognised event received in MainDisplay: " + event);
		}
	}

	/**
	 * No arguments allowed!
	 * @param args
	 */
	public static void main(String[] args) {
		MainDisplay display = new MainDisplay();
		display.run();
	}

	

}
