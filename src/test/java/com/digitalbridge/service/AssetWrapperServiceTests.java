package com.digitalbridge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.digitalbridge.DigitalBridgeApplicationTests;
import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.domain.Notes;
import com.digitalbridge.request.UpdateRequest;
import com.digitalbridge.security.SecurityUtils;

public class AssetWrapperServiceTests extends DigitalBridgeApplicationTests {

	@Test
	public final void testGetAll() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		assetWrapperService.getAll();
	}

	@Test
	public final void testGeospatialsearch() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		Page<AssetWrapper> response = assetWrapperService.geospatialsearch();
		assertTrue(response.hasContent());
	}

	@Test
	public final void testCreateGeoSpatialIndex() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_ADMIN);
		assetWrapperService.createGeoSpatialIndex();
	}

	@Test
	public void testSetUpdateValue() throws Exception {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.setKey("aName");
		updateRequest.setValue("Matt'S Grill Restaurant");
		UpdateRequest updateRequest2 = new UpdateRequest();
		updateRequest2.setKey("cuisine");
		updateRequest2.setValue("Indian");
		AssetWrapper assetWrapper = assetWrapperService.updateIndividualFields(assetID,
				Arrays.asList(updateRequest, updateRequest2));
		assertTrue(assetWrapper.getId().equalsIgnoreCase(assetID));
	}

	@Test
	public void testAddNotesandDeleteNotes() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		Map<String, Object> value = new HashMap<String, Object>();
		Notes notes = new Notes("R", new Date(1441712050500l), 21);
		notesRepository.save(notes);
		value.put("notes", notes);
		AssetWrapper assetWrapper = assetWrapperRepository.findOne(assetID);
		int originalCount = assetWrapper.getNotes().size();
		AssetWrapper restaurants = assetWrapperService.addToFieldArray(assetID, value);
		assertTrue(restaurants.getId().equalsIgnoreCase(assetID));
		assertEquals(assetWrapperRepository.findOne(assetID).getNotes().size(),
				originalCount + 1);
		assetWrapperService.removeFromFieldArray(assetID, value);
		assertEquals(assetWrapperRepository.findOne(assetID).getNotes().size(),
				originalCount);
	}

}
