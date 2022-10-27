package com.example.demo;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

import java.security.Security;

public class BouncyCastleFeature implements Feature {

  @Override
  public void afterRegistration(AfterRegistrationAccess access) {
    RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
    Security.addProvider(new BouncyCastleProvider());
  }

}