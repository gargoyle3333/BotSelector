package net.sim.classes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Vector3f;

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
	/*
	 * Finds the normal to the triangle abc
	 *     normalize( (b - a) cross (c - a) )
	 */
	public static Vector3f normalTo(Vector3f a, Vector3f b, Vector3f c) {
		// do this with as few vectors as possible
		Vector3f sideAC = new Vector3f(),
		         sideAB = new Vector3f(); // also represents -a
		
		a.negate(sideAC);
		Vector3f.add(b, sideAC, sideAB);
		Vector3f.add(c, sideAC, sideAC);
		return (Vector3f) Vector3f.cross(sideAB, sideAC, sideAB).normalise();
	}
}
