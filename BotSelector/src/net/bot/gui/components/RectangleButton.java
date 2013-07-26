package net.bot.gui.components;

import net.bot.event.listener.IDisplayEventListener;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;

public class RectangleButton {
	
	public Vector2f cornerTopLeft, cornerBottomRight;
	public Color mDefaultColor, mHighlightedColor;
	private final IButtonHandler mButtonHandler;
	
	
	
	public RectangleButton(Vector2f topLeft, Vector2f bottomRight, 
			Color defaultColor, Color highlightedColor, IButtonHandler handler) {
		
		cornerTopLeft = topLeft;
		cornerBottomRight = bottomRight;
		
		mDefaultColor = defaultColor;
		mHighlightedColor = highlightedColor;
		
		mButtonHandler = handler;
		
		
		
	}
	
	public void cleanup() {
		// As is
	}
	
}
