package net.bot.gui;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

public class LoadingScreen extends BaseScreen {
	
	public LoadingScreen(MasterScreen master) {
		super(master);
		// Set up viewpoint
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// x: 0 to WIDTH; y: 0 to HEIGHT; z: 1 to -1;
		glOrtho(0, 1, 0, 1, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
	}
	
	@Override
	public String getTitle() {
		return "Loading Screen";
	}

}
