package net.sim.classes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.util.ReadableColor;

public class Utils {
	private static ByteBuffer _buffer16;
	
	static {
		_buffer16 = ByteBuffer.allocateDirect(16);
		_buffer16.order(ByteOrder.nativeOrder());
	}
	/*
	 * Writes 4 floats into a FloatBuffer
	 */
	public static FloatBuffer fBuffer4(float a, float b, float c, float d) {
		return (FloatBuffer) _buffer16.asFloatBuffer()
				.put(a).put(b).put(c).put(d)
				.flip();
	}
	/*
	 * Write a color into a FloatBuffer
	 */
	public static FloatBuffer fBuffer4(ReadableColor c) {
		return (FloatBuffer) _buffer16.asFloatBuffer()
			.put(c.getRed() / 255f).put(c.getGreen() / 255f).put(c.getBlue() / 255f).put(c.getAlpha() / 255f)
			.flip();
	}
}
