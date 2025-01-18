package com.ll.restbytdd.util;

public class Ut {
	public static class str {
		public static boolean isBlank(String str) {
			return str == null || str.trim().isEmpty();
		}
	}
}
