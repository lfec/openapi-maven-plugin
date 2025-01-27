package io.github.kbuntrock.resources.dto.genericity.extendsMap;

import java.util.HashMap;
import java.util.UUID;

/**
 * A map between Integer and UUID
 */
public class ExtendsMapUUID extends HashMap<Integer, UUID> {

	/**
	 * Since this class is a list, this attribute is ignored
	 */
	private String someUnusedString;

	public String getSomeUnusedString() {
		return someUnusedString;
	}

	public void setSomeUnusedString(String someUnusedString) {
		this.someUnusedString = someUnusedString;
	}
}
