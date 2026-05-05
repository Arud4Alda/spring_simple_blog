package com.blog.vo;

public class Result {
	int index;
	String message;

	public Result() {
	}

	public Result(int index, String message) {
		this.index = index;
		this.message = message;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
