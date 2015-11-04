package com.digitalbridge.domain;

import java.io.IOException;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * <p>
 * GrantedAuthoritySerializer class.
 * </p>
 *
 * @author rajakolli
 * @version 1: 0
 */
public class GrantedAuthoritySerializer extends JsonSerializer<GrantedAuthority> {

	/** {@inheritDoc} */
	@Override
	public void serialize(GrantedAuthority value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeString(value.getAuthority());
	}
}
