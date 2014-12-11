package sfw.elasticsearch.common;

public enum Messages {

	SUCCESS(200,"Success"),
	ERROR(10001,"Unknow error"),
	NO_INDEX_DEFINE(10002, "No index defined"),
	NO_TYPE_DEFINE(10003, "No type defined"),
	NO_ID_DEFINE(10004,"No Id defined"),
	INDEX_EXISTS(10005,"Index is already exists"),
	NO_INDEX_EXISTS(10006, "No index exists"),
	NO_TYPE_EXISTS(10007, "No type exists"),
	NO_ID_EXISTS(10008,"No Id exists"),
	DATA_FORMAT_ERROR(10009,"Data format error"),
	NOT_EMPTY(10010," cannot be empty");

	private final int code;
	private final String description;

	private Messages(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}
}