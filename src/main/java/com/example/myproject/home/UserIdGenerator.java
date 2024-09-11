package com.example.myproject.home;

import java.util.Random;

public class UserIdGenerator {

	public static String sampleSpace = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static String generateId() {
		Random random = new Random();
		char[] chars = new char[20];
		for (int i = 0; i < 20; i++) {
			int index = random.nextInt(sampleSpace.length());
			chars[i] = sampleSpace.charAt(index);
		}
		return new String(chars);
	}

	public static void main(String[] args) {
		String id = generateId();
		System.out.println(id);
	}

}
