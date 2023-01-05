package com.example.demo;

import org.springframework.batch.core.aot.CoreRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
public class SpringBatchGraalvmDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBatchGraalvmDemoApplication.class, args);
  }
  
}
