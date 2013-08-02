package net.bot.gui.components;

import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.MouseEventHandler;
import net.bot.event.listener.IDisplayEventListener;
import net.bot.event.listener.IMouseEventListener;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;
import static org.lwjgl.opengl.GL11.*;

public class RectangleButton {
	
	public Vector2f cornerTopLeft, cornerBottomRight;
	public Color mDefaultColor, mHighlightedColor;
	private final IButtonHandler mButtonHandler;
	
	private boolean mouseOver = false, mouseClickedOver = false;
	
	public RectangleButton(Vector2f topLeft, Vector2f bottomRight, 
			Color defaultColor, Color highlightedColor, IButtonHandler handler) {
		
		cornerTopLeft = topLeft;
		cornerBottomRight = bottomRight;
		
		mDefaultColor = defaultColor;
		mHighlightedColor = highlightedColor;
		
		mButtonHandler = handler;
		
		IMouseEventListener mouseListener = new IMouseEventListener() {
			@Override
			public void mouseReleased(float x, float y) {
				if (mouseOver && mouseClickedOver) {
					// handle events
					mButtonHandler.onButtonClicked();
				} else {
					mouseClickedOver = false;
					// Not really necessary, but let's be tidy
				}
			}
			
			@Override
			public void mouseMoved(float x, float y, float dX, float dY) {
				float scaledX = x / Display.getWidth();
				float scaledY = y / Display.getHeight();
				mouseOver = scaledX > cornerTopLeft.x && scaledX < cornerBottomRight.x
						&& scaledY < cornerTopLeft.y && scaledY > cornerBottomRight.y;
			}
			
			@Override
			public void mouseDragged(float x, float y, float dX, float dY) {
				// Doesn't affect us
			}
			
			@Override
			public void mouseClicked(float x, float y) {
				mouseClickedOver = mouseOver;
			}
		};
		
		IDisplayEventListener displayListener = new IDisplayEventListener() {
			@Override
			public void onUpdate(double delta) {
				glBegin(GL_QUADS);
				
				Color current = mouseOver ? mHighlightedColor : mDefaultColor;
				glColor3f(current.getRed(), current.getGreen(), current.getBlue());
				
				glVertex2f(cornerTopLeft.x, cornerBottomRight.y);
				glVertex2f(cornerTopLeft.x, cornerTopLeft.y);
				glVertex2f(cornerBottomRight.x, cornerTopLeft.y);
				glVertex2f(cornerBottomRight.x, cornerBottomRight.y);
				
				glEnd();
			}
		};
		
		MouseEventHandler.addListener(mouseListener);
		DisplayEventHandler.addListener(displayListener);
		
	}
	
}
