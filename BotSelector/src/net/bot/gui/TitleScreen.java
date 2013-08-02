package net.bot.gui;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.KeyboardEventHandler;
import net.bot.event.handler.MouseEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IKeyboardEventListener;
import net.bot.event.listener.IMouseEventListener;
import net.bot.input.KeyboardInput;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

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
		
		mDisplayListener = new IDisplayEventListener() {
			
			@Override
			public void onUpdate(double delta) {
				// Erm... Draw a triangle?
				GL11.glBegin(GL11.GL_TRIANGLES);
				
				GL11.glColor3f(1, 1, 1);
				GL11.glVertex2f(0.25f, 0.25f);
				GL11.glVertex2f(0.75f, 0.25f);
				GL11.glVertex2f(0.25f, 0.75f);
				
				GL11.glEnd();
			}
		};
		DisplayEventHandler.addListener(mDisplayListener);
		
	}
	
	@Override
	public String getTitle() {
		return "Title Screen";
	}

//	@Override
//	public void cleanup() {
//		// Garbage collectors handles dead objects
//		DisplayEventHandler.removeListener(mDisplayListener);
//	}
	
}
