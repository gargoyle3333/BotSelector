package net.bot.input;

import net.bot.event.handler.DisplayEventHandler;
import net.bot.event.handler.MouseEventHandler;
import net.bot.event.listener.IDisplayEventListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseInput {
	
	public static final int LEFT_BUTTON = 0;
	public static final int RIGHT_BUTTON = 1;
	public static final int SCROLL_WHEEL = 2;
	
	public MouseInput() {
		if (!Mouse.isCreated()) {
			System.out.println("Have to create mouse");
			try {
				Mouse.create();
			} catch (LWJGLException e) {
				System.err.println("FAILED TO CREATE MOUSE");
			}
		}
		DisplayEventHandler.addListener(new IDisplayEventListener() {
			@Override
			public void onUpdate(double delta, boolean paused) {
				pollMouse();
			}
		});
	}
	
	public void pollMouse() {
		int eventButton = -1;
		float dX, dY, absX, absY;
		while (Mouse.next()) {
			eventButton = Mouse.getEventButton();
			absX = Mouse.getEventX() / (float)Display.getWidth();
			absY = Mouse.getEventY() / (float)Display.getHeight();
			dX = Mouse.getEventDX() / (float)Display.getWidth();
			dY = Mouse.getEventDY() / (float)Display.getHeight();
			if (eventButton != -1) {
				switch (eventButton) {
				case LEFT_BUTTON: 
					if (Mouse.getEventButtonState()) {
						MouseEventHandler.leftButtonPressed(absX, absY);
					} else {
						MouseEventHandler.leftButtonReleased(absX, absY);
					}
					break;
				case RIGHT_BUTTON: 
					if (Mouse.getEventButtonState()) {
						MouseEventHandler.rightButtonPressed(absX, absY);
					} else {
						MouseEventHandler.rightButtonReleased(absX, absY);
					}
					break;
				case SCROLL_WHEEL: 
					if (Mouse.getEventButtonState()) {
						MouseEventHandler.scrollWheelPressed(absX, absY);
					} else {
						MouseEventHandler.scrollWheelReleased(absX, absY);
					}
					break;
				default:
					System.out.println("Unidentified button constant: " + eventButton);
				}
					
				//System.out.println("New event button code: " + Mouse.getEventButton());
			}
			else {
				MouseEventHandler.mouseMove(dX, dY, absX, absY);
				/*
				System.out.println(String.format("Mouse moved. dX=%d dY=%d absX=%d absY=%d", 
						Mouse.getEventDX(),
						Mouse.getEventDY(),
						Mouse.getEventX(),
						Mouse.getEventY())); */
			}
		}
	}
	
}
