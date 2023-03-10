package com.example.demo;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

//Ensures that Bouncycastle is initilized at build time
public class BouncyCastleFeature implements Feature {
  @Override
  public void afterRegistration(AfterRegistrationAccess access) {
    RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
    Security.addProvider(new BouncyCastleProvider());
  }
}