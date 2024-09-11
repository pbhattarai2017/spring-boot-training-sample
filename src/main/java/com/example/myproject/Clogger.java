package com.example.myproject;

public class Clogger {
	public static void info(Object msg) {
		System.out.println("\033[1;32mINFO:\033[0m " + msg);
		System.out.flush();
	}

	public static void error(Object msg) {
		System.out.println("\033[1;31mERROR:\033[0m " + msg);
		System.out.flush();
	}
}
