package com.libriciel.Atteste.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NotificationTest {

	@Test
	public void test() {
		Notification n = new Notification();
		assertNull(n.getObjet());
		assertNull(n.getMessage());
	}

}
