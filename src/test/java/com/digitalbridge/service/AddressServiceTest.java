package com.digitalbridge.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bson.Document;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import com.digitalbridge.MongoESConfigTest;
import com.digitalbridge.domain.Address;
import com.digitalbridge.domain.AssetWrapper;
import com.digitalbridge.security.SecurityUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;

public class AddressServiceTest extends MongoESConfigTest {

	@Test
	public final void testUpdateSetValue() {

		SecurityUtils.runAs(USERNAME, PASSWORD, ROLE_USER);
		Map<String, Object> value = new HashMap<String, Object>();
		int random = new Random().nextInt(1000);
		value.put("building", random);
		Address address = addressService.updateSetValue(assetID, value);
		assertTrue(address.getBuilding().equalsIgnoreCase(String.valueOf(random)));
		Page<AssetWrapper> res = assetWrapperRepository
				.findByAddressIdIn(Arrays.asList(address.getId()), pageable);
		assertEquals(assetID, res.getContent().get(0).getId());

	}

	@Test
	@Ignore
	public void testCreateIndex() throws Exception {
		MongoCollection<Document> collection = mongoClient.getDatabase("digitalbridge")
				.getCollection("address", Document.class);
		// Document bkey = Document.parse("{ \"location\": \"2dsphere\" }");
		Document bkey = new Document("loaction", "2dsphere");
		collection.dropIndexes();
		collection.createIndex(bkey, new IndexOptions().name("geospatialIdx"));
	}

	@Test
	public void testGeoNear() {
		GeoResults<Address> result = addressService.geoNear(new Point(-73, 40), 65);
		assertThat(result.getContent().size(), is(not(0)));
		assertThat(result.getAverageDistance().getMetric(), is((Metric) Metrics.MILES));
	}

}
