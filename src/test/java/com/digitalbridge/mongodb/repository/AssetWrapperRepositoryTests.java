package com.digitalbridge.mongodb.repository;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.digitalbridge.DigitalBridgeApplicationTests;
import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.request.UpdateRequest;
import com.digitalbridge.security.SecurityUtils;

public class AssetWrapperRepositoryTests extends DigitalBridgeApplicationTests {

	@Test
	public final void testUpdate() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		AssetWrapper assetWrapper = assetWrapperRepository.findOne(assetID);
		Long initialVersion = assetWrapper.getVersion();
		String originalName = assetWrapper.getAssetName();
		int notesCount = assetWrapper.getNotes().size();
		List<UpdateRequest> updateRequestList = Arrays.asList(new UpdateRequest("aName","Customized Asset"));
		assetWrapperService.updateIndividualFields(assetID, updateRequestList);
		AssetWrapper updatedAssetWrapper = assetWrapperRepository.findOne(assetID);
		assertTrue(updatedAssetWrapper.getVersion() == initialVersion + 1);
		assertTrue(originalName != updatedAssetWrapper.getAssetName());
		assertTrue(notesCount == updatedAssetWrapper.getNotes().size());
	}

	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public final void testUpdateFail() {
		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		AssetWrapper assetWrapper = assetWrapperRepository.findOne(assetID);
		assetWrapper.setAssetName("Customized Asset");
		SecurityContextHolder.clearContext();
		assetWrapperRepository.save(assetWrapper);
	}

}
