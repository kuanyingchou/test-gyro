package com.kuanying.testgyro;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestHello {

	@Test
	public void testMultiply() {
		//fail("Not yet implemented");
		assertArrayEquals(Util.multiply(new float[] {1, 1, 1}, new float[] {
			3, 4, 5,
			4, 5, 6,
			5, 6, 7
		}),
		new float[] {12, 15, 18},
		0.00001f);
		
		assertArrayEquals(Util.multiply(new float[] {3, 4, 5}, new float[] {
				1, 0, 0,
				0, 1, 0,
				0, 0, 1
			}),
			new float[] {3, 4, 5},
			0.00001f);
	}

}
