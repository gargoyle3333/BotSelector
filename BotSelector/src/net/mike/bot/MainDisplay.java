package net.mike.bot;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainDisplay {
	
	private static final int FRAMES_PER_SECOND = 60;
	
	private static final String GAME_TITLE = "Bot Simulation";

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	public MainDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(GAME_TITLE);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// Set up OpenGL here
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	}
	
	public void run() {
		
		while (!Display.isCloseRequested()) {
			// Update
			// Redraw
			GL11.glClearColor(0.2F, 0.2F, 0.2F, 0F);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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
