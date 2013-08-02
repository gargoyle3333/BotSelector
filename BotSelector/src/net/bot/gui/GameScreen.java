package net.bot.gui;

import net.bot.SimController;
import net.bot.SimRegister;
import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.KeyboardEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IKeyboardEventListener;

import static net.bot.util.MainDisplayConstants.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class GameScreen extends BaseScreen {
	
	private boolean[] arrowKeysPressed;
	
	private SimController mController;
	
	// Listeners
	private IKeyboardEventListener mKeyboardListener;
	private IDisplayEventListener mDisplayListener;
	
	public GameScreen(MasterScreen master) {
		super(master);
		// Set up viewpoint
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// x: 0 to WIDTH; y: 0 to HEIGHT; z: 1 to -1;
		glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		arrowKeysPressed = new boolean[4];
		final Vector2f offset = new Vector2f(0,0);
		
		mController = new SimController();
		
		mKeyboardListener = new IKeyboardEventListener() {
			
			@Override
			public void onKeyReleased(int key) {
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
				default:
					break;
				}				
			}
			
			@Override
			public void onKeyPressed(int key) {
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
		};
		mDisplayListener = new IDisplayEventListener() {
			
			@Override
			public void onUpdate(double delta) {
				updateViewOffset(arrowKeysPressed, offset);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(offset.x, offset.x + SCREEN_WIDTH, offset.y, offset.y + SCREEN_HEIGHT, 1, -1);
				glMatrixMode(GL_MODELVIEW);
			}
		};
		
		KeyboardEventHandler.addListener(mKeyboardListener);
		DisplayEventHandler.addListener(mDisplayListener);
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

	@Override
	public String getTitle() {
		return "Main Game";
	}

//	@Override
//	public void cleanup() {
//		// Tell our associated objects to cleanup
//		mController.cleanup();
//		// We just need to remove listeners
//		KeyboardEventHandler.removeListener(mKeyboardListener);
//		DisplayEventHandler.removeListener(mDisplayListener);
//	}
	
}
