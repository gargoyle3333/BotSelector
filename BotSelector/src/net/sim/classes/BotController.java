package net.sim.classes;

import java.util.Random;

import net.sim.interfaces.BotKeyboardListener;
import net.sim.interfaces.BotMouseListener;
import net.sim.interfaces.BotWindowListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Intended as the presenter/controller mechanism for bots. Each new bot is
 * constructed with this as a parameter.
 * 
 * @author mrh2
 * 
 */
public class BotController implements BotMouseListener, BotKeyboardListener, BotWindowListener {

	private static final int INITIAL_BOT_POPULATION = 20, FOOD_SPECKS_AVAILABLE = 20;
	
	private Random mRandom;
	private int xMax, yMax, thetaMax, sizeMax, sizeMin;
	
	//Variables for structured game programming
	private GameBoard mGameBoard;
	private SimRegister mSimRegister;

	public BotController() throws LWJGLException {
		mGameBoard = new GameBoard(this, this);
		
		mRandom = new Random();
		xMax = Display.getWidth();
		yMax = Display.getHeight();
		thetaMax = 360;
		sizeMax = 20;
		sizeMin = 5;

		mSimRegister = new SimRegister();
		for (int i = 0; i < INITIAL_BOT_POPULATION; i++) {
			new Bot(this,
					mRandom.nextInt(xMax),
					mRandom.nextInt(yMax),
					Math.toRadians(mRandom.nextInt(thetaMax) - (thetaMax/2 -1)),
					mRandom.nextInt(sizeMax - sizeMin) + sizeMin + 1);
		}
		for (int i = 0; i < FOOD_SPECKS_AVAILABLE; i++) {
			new FoodSpeck(this,
					mRandom.nextInt(xMax),
					mRandom.nextInt(yMax),
					Math.toRadians(mRandom.nextInt(thetaMax) - (thetaMax/2 -1)));
		}
	}
	
	public void start(){
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			mSimRegister.update();
			mGameBoard.update();
		}
	}

	public void register(Bot bot) {
		mSimRegister.registerBot(bot);
	}
	
	public void register(FoodSpeck speck) {
		mSimRegister.registerSpeck(speck);
	}

	@Override
	public void keyPressed(int key) {
		switch (key) {
		case Keyboard.KEY_UP:
			mSimRegister.setSelectedMovingForward(true);
			break;
		case Keyboard.KEY_RIGHT:
			mSimRegister.setSelectedRotatingClockwise(true);
			break;
		case Keyboard.KEY_LEFT:
			mSimRegister.setSelectedRotatingAntiClockwise(true);
			break;
//		default:
//			System.out.printf("Key pressed: %s\n", Keyboard.getKeyName(key));
		}
//		System.out.printf("Key pressed: %s\n", Keyboard.getKeyName(key));
	}

	@Override
	public void keyReleased(int key) {
		switch (key) {
		case Keyboard.KEY_UP:
			mSimRegister.setSelectedMovingForward(false);
			break;
		case Keyboard.KEY_RIGHT:
			mSimRegister.setSelectedRotatingClockwise(false);
			break;
		case Keyboard.KEY_LEFT:
			mSimRegister.setSelectedRotatingAntiClockwise(false);
			break;
		}
//		System.out.printf("Key released: %s\n", Keyboard.getKeyName(key));
	}

	@Override
	public void keyTyped(int key) {
		// No op
	}

	@Override
	public void leftButtonClicked(int x, int y) {
		mSimRegister.pickupBotNearest(x,y);
//		System.out.printf("Left mouse button clicked at (%d,%d)\n", x,y);
	}

	@Override
	public void leftButtonReleased(int x, int y) {
		mSimRegister.release();
	}

	@Override
	public void rightButtonClicked(int x, int y) {
//		System.out.printf("Right mouse button clicked at (%d,%d)\n", x,y);
	}

	@Override
	public void rightButtonReleased(int x, int y) {
//		System.out.printf("Right mouse button released at (%d,%d)\n", x,y);
		
	}

	@Override
	public void leftDragged(int x, int y) {
		mSimRegister.dragSelected(x,y);
	}

	@Override
	public void rightDragged(int x, int y) {
//		System.out.printf("Right mouse button dragged to (%d,%d)\n", x,y);
	}
	
	public static void main(String[] args) throws LWJGLException {
		BotController botController = new BotController();
		botController.start();
	}

	@Override
	public void windowResized(int width, int height) {
		mSimRegister.updateScreenSize();
	}

}
