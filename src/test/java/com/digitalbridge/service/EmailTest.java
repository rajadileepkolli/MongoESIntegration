package com.digitalbridge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.digitalbridge.MongoESConfigTest;

public class EmailTest extends MongoESConfigTest{

  @Autowired Email email;
  @Test
  public final void testSendEmail() {
    email.sendEmail();
  }

}
