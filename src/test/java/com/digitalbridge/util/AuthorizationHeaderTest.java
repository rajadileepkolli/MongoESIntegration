package com.digitalbridge.util;

import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class AuthorizationHeaderTest {

	AuthorizationHeader authorizationHeader = new AuthorizationHeader();

	@Test
	public final void testGetHeader() throws UnsupportedEncodingException {
		String val = authorizationHeader.getBasicHeader("appUser", "appPassword");
		assertNotNull(val);
	}

}
