package net.bot.input;

import net.bot.event.handler.MouseEventHandler;

import org.lwjgl.input.Mouse;

public class MouseInput {
	
	private static final float MOUSE_SENSITIVITY = 4;
	private float lastX = 0, lastY = 0;
	private boolean mouseDown = false;
	
	public void pollMouse() {
		
		// Check for clicks
		boolean leftButton = Mouse.isButtonDown(0);
		if (mouseDown != leftButton) {
			mouseDown = leftButton;
			if (mouseDown) {
				MouseEventHandler.mouseClicked(Mouse.getX(), Mouse.getY());
			} else {
				MouseEventHandler.mouseReleased(Mouse.getX(), Mouse.getY());
			}
		}
		
		// Check for movement
		// We can't rely on dX or dY here, as they won't listen to our sensitivity
		float dX = Mouse.getX() - lastX;
		float dY = Mouse.getY() - lastY;
		if(Math.abs(dX) > MOUSE_SENSITIVITY ||
				Math.abs(dY) > MOUSE_SENSITIVITY) {
			lastX = Mouse.getX();
			lastY = Mouse.getY();
			MouseEventHandler.mouseMoved(lastX, lastY, dX, dY);
			if (mouseDown) {
				MouseEventHandler.mouseDragged(lastX, lastY, dX, dY);
			}
		}
		
	}

}
