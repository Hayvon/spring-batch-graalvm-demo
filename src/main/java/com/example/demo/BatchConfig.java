package com.example.demo;

import javax.sql.DataSource;

import com.example.demo.BatchConfig.CustomRuntimeHints;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.DecoratingProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ImportRuntimeHints(CustomRuntimeHints.class)
public class BatchConfig {

  @Bean
  public Job job(JobRepository jobRepository, Step step1, Step sshStep) {
    return new JobBuilder("importUserJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(step1)
        //.next(sshStep)
        .build();
  }

  /*@Bean
  public Step sshStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder("sshStep", jobRepository)
        .tasklet(new SSHTasklet())
        .transactionManager(platformTransactionManager)
        .build();
  }*/

  @Bean
  public Step step1(
      JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
      JdbcBatchItemWriter<Person> writer
  ) {
    return new StepBuilder("step1", jobRepository)
        .<Person, Person>chunk(10)
        .transactionManager(platformTransactionManager)
        .reader(reader())
        .processor(new PersonItemProcessor())
        .writer(writer)
        .build();
  }

  @Bean
  public FlatFileItemReader<Person> reader() {
    return new FlatFileItemReaderBuilder<Person>()
        .name("personReader")
        .resource(new ClassPathResource("sample-data.csv"))
        .delimited()
        .names("name", "surname")
        .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
          setTargetType(Person.class);
        }})
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO people (name, surname) VALUES (:name, :surname)")
        .dataSource(dataSource)
        .build();
  }

  static class CustomRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {
      hints.proxies()
          .registerJdkProxy(builder -> builder
              .proxiedInterfaces(TypeReference.of("org.springframework.batch.core.launch.JobOperator"))
              .proxiedInterfaces(SpringProxy.class, Advised.class, DecoratingProxy.class));
    }
  }
}
