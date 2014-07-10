package com.kuanying.testgyro;

public class Util {
	public static float[] multiply(float[] vector, float[] matrix) {
		float[] result = new float[vector.length];
		for(int i=0; i<vector.length; i++) {
			float sum = 0;
			for(int j=0; j<vector.length; j++) {
				sum += vector[i] * matrix[j*vector.length+i];
			}
			result[i] = sum;
		}
		return result;
	}

}
