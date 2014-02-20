package net.bot;

import net.bot.entities.EntityBot;
import net.bot.entities.EntityDiseasedBot;
import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.handler.KeyboardEventHandler;
import net.bot.event.listener.IKeyboardEventListener;
import static net.bot.util.MainDisplayConstants.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class MainDisplay {
	
	private double lastFrameTime = System.currentTimeMillis();
	
	private boolean[] arrowKeysPressed;
	
	public MainDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle(GAME_TITLE);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Set up viewpoint
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// x: 0 to WIDTH; y: 0 to HEIGHT; z: 1 to -1;
		glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		arrowKeysPressed = new boolean[4];
		
		KeyboardEventHandler.addListener(new IKeyboardEventListener() {
			@Override
			public void onKeyPressed(int key) {
				switch (key) {
				case Keyboard.KEY_UP: 
					arrowKeysPressed[0] = true;
					break;
				case Keyboard.KEY_DOWN: 
					arrowKeysPressed[1] = true;
					break;
				case Keyboard.KEY_LEFT: 
					arrowKeysPressed[2] = true;
					break;
				case Keyboard.KEY_RIGHT: 
					arrowKeysPressed[3] = true;
					break;
				case Keyboard.KEY_R:
					EntityBot redBot = new EntityBot();
					redBot.setColor(new Color(255,0,0));
					EntityEventHandler.botCreated(redBot);
					break;
				case Keyboard.KEY_B:
					EntityBot blueBot = new EntityBot();
					blueBot.setColor(new Color(0,0,255));
					EntityEventHandler.botCreated(blueBot);
					break;
				default:
					break;
				}
			}
			@Override
			public void onKeyReleased(int key) {
				switch(key) {
				case Keyboard.KEY_UP: 
					arrowKeysPressed[0] = false;
					break;
				case Keyboard.KEY_DOWN: 
					arrowKeysPressed[1] = false;
					break;
				case Keyboard.KEY_LEFT: 
					arrowKeysPressed[2] = false;
					break;
				case Keyboard.KEY_RIGHT: 
					arrowKeysPressed[3] = false;
					break;
				}
			}
		});
	}
	
	public void run() {
		
		Vector2f viewOffset = new Vector2f();
		
		while (!Display.isCloseRequested()) {
			
			// Move screen based on arrow keys
			updateViewOffset(arrowKeysPressed, viewOffset);
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(0.2F, 0.2F, 0.2F, 0F);
			
			// Set the viewpoint
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(viewOffset.x, viewOffset.x + SCREEN_WIDTH, viewOffset.y, viewOffset.y + SCREEN_HEIGHT, 1, -1);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
			// Update
			DisplayEventHandler.update(getDelta());
			
			Display.update();
			Display.sync(FRAMES_PER_SECOND); 
		}
		
		// Cleanup
		Display.destroy();
		
	}
	
	private double getDelta() {
		double delta = System.currentTimeMillis() - lastFrameTime;
		lastFrameTime = System.currentTimeMillis();
		return delta;
	}
	
	private void updateViewOffset(boolean[] directions, Vector2f offset) {
		// Up
		if (directions[0]) {
			offset.y += SCREEN_PAN;
			if (offset.y > BOARD_HEIGHT - SCREEN_HEIGHT) {
				offset.y = (float) (BOARD_HEIGHT - SCREEN_HEIGHT);
			}
		}
		// Down
		if (directions[1]) {
			offset.y -= SCREEN_PAN;
			if (offset.y < 0) offset.y = 0;
		}
		// Left
		if (directions[2]) {
			offset.x -= SCREEN_PAN;
			if (offset.x < 0) offset.x = 0;
		}
		// Right
		if (directions[3]) {
			offset.x += SCREEN_PAN;
			if (offset.x > BOARD_WIDTH - SCREEN_WIDTH) {
				offset.x = (float) (BOARD_WIDTH - SCREEN_WIDTH);
			}
		}
	}
	
}
