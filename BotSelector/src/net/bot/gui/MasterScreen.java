package net.bot.gui;

import static net.bot.util.MainDisplayConstants.FRAMES_PER_SECOND;
import static net.bot.util.MainDisplayConstants.SCREEN_HEIGHT;
import static net.bot.util.MainDisplayConstants.SCREEN_WIDTH;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.EntityEventHandler;
import net.bot.event.handler.KeyboardEventHandler;
import net.bot.event.handler.MouseEventHandler;
import net.bot.event.listener.IKeyboardEventListener;
import net.bot.input.KeyboardInput;
import net.bot.input.MouseInput;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class MasterScreen {
	
	private static final String baseTitle = "Bot Selector - ";
	
	public enum ScreenState {
		LOADING, TITLE, GAME
	}
	
	private Map<ScreenState, Class<?>> screenMap;

	private ScreenState mScreenState = ScreenState.LOADING;
	private BaseScreen mCurrentScreen;
	private double lastFrameTime = System.currentTimeMillis();

	public MasterScreen() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
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
		
		// Set up our screen map
		screenMap = new HashMap<ScreenState, Class<?>>();
		screenMap.put(ScreenState.LOADING, LoadingScreen.class);
		screenMap.put(ScreenState.TITLE, TitleScreen.class);
		screenMap.put(ScreenState.GAME, GameScreen.class);
		
		// Set up keyboard listener
		KeyboardEventHandler.addListener(new IKeyboardEventListener() {
			@Override
			public void onKeyReleased(int key) {
				
			}
			
			@Override
			public void onKeyPressed(int key) {
				if (key == Keyboard.KEY_SPACE) {
					changeScreen(ScreenState.GAME);
				} else if (key == Keyboard.KEY_ESCAPE) {
					changeScreen(ScreenState.TITLE);
				} else if (key == Keyboard.KEY_RCONTROL) {
					changeScreen(ScreenState.LOADING);
				}
			}
		});
	}
	
	private void run() {

		// We're going to start in the title screen.
//		mCurrentScreen = new TitleScreen(this);
		changeScreen(ScreenState.TITLE);
		KeyboardInput keyboardInput = new KeyboardInput();
		MouseInput mouseInput = new MouseInput();
		
		while (!Display.isCloseRequested()) {

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(0.2F, 0.2F, 0.2F, 0F);

			// Update
			keyboardInput.pollKeyboard();
			mouseInput.pollMouse();
			DisplayEventHandler.update(getDelta());

			Display.update();
			Display.sync(FRAMES_PER_SECOND);
		}
		
	}
	
	public void changeScreen(ScreenState ss) {
		if(mScreenState == ss) { 
			return;
		}
//		mCurrentScreen.cleanup();
		clearListeners();
		try {
			try {
				mCurrentScreen = (BaseScreen) screenMap.get(ss).getConstructor(MasterScreen.class).newInstance(this);
			} catch (IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				System.exit(1);
			}
			mScreenState = ss;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Display.setTitle(baseTitle + mCurrentScreen.getTitle());
	}

	private double getDelta() {
		double delta = System.currentTimeMillis() - lastFrameTime;
		lastFrameTime = System.currentTimeMillis();
		return delta;
	}
	
	private void clearListeners() {
		DisplayEventHandler.clearListeners();
		EntityEventHandler.clearListeners();
		KeyboardEventHandler.clearListeners();
		MouseEventHandler.clearListeners();
	}

	public static void main(String[] args) {
		MasterScreen masterScreen = new MasterScreen();
		masterScreen.run();
	}

	
	
}

