package net.bot.gui;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.gui.MasterScreen.ScreenState;
import net.bot.gui.components.IButtonHandler;
import net.bot.gui.components.RectangleButton;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class TitleScreen extends BaseScreen {
	
	private IDisplayEventListener mDisplayListener;
	
	public TitleScreen(MasterScreen master) {
		super(master);
		// Set up viewpoint
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// x: 0 to WIDTH; y: 0 to HEIGHT; z: 1 to -1;
		glOrtho(0, 1, 0, 1, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		new RectangleButton(
				new Vector2f(0.25f, 0.9f), 
				new Vector2f(0.75f, 0.8f), 
				new Color(1,0,0), 
				new Color(0,1,0), 
				new IButtonHandler() {
			@Override
			public void onButtonClicked() {
				mMasterScreen.changeScreen(ScreenState.GAME);
			}
		});
		
	}
	
	@Override
	public String getTitle() {
		return "Title Screen";
	}

}
