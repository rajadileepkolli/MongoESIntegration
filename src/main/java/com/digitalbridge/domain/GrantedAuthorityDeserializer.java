package com.digitalbridge.domain;

import java.io.IOException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * <p>
 * GrantedAuthorityDeserializer class.
 * </p>
 *
 * @author rajakolli
 * @version 1: 0
 */
public class GrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {
	/** {@inheritDoc} */
	@Override
	public SimpleGrantedAuthority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		return new SimpleGrantedAuthority(jp.getValueAsString());
	}
}
