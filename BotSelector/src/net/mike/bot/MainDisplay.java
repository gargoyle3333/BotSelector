package net.mike.bot;

import net.mike.bot.event.Event;
import net.mike.bot.event.GlobalEventHandler;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainDisplay {
	
	private static final int FRAMES_PER_SECOND = 60;
	
	private static final String GAME_TITLE = "Bot Simulation";

	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = 600;
	
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
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
		
		new SimController();
		
	}
	
	public void run() {
		
		while (!Display.isCloseRequested()) {
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.2F, 0.2F, 0.2F, 0F);
			
			int xOffset = 0, yOffset = 0;
			// Set the viewpoint
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(xOffset, xOffset + SCREEN_WIDTH, yOffset, yOffset + SCREEN_HEIGHT, 1, -1);
			
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

	/**
	 * No arguments allowed!
	 * @param args
	 */
	public static void main(String[] args) {
		MainDisplay display = new MainDisplay();
		display.run();
	}

}
