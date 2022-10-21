package com.example.demo;

import java.sql.Types;
import java.util.Objects;
import javax.sql.DataSource;

import com.example.demo.BatchConfig.BatchRuntimeHints;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.aot.CoreRuntimeHints;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
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
import org.springframework.util.ReflectionUtils;

@Configuration
//@ImportRuntimeHints(BatchRuntimeHints.class)
public class BatchConfig {

    @Bean
    public Job job(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
        JdbcBatchItemWriter<Person> writer) {
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

    static class BatchRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            /*hints.resources().registerPattern("org/springframework/batch/core/schema-h2.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-derby.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-hsqldb.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-sqlite.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-db2.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-hana.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-mysql.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-oracle.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-postgresql.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-sqlserver.sql");
            hints.resources().registerPattern("org/springframework/batch/core/schema-sybase.sql");

            hints.proxies()
                .registerJdkProxy(builder -> builder
                    .proxiedInterfaces(TypeReference.of("org.springframework.batch.core.repository.JobRepository"))
                    .proxiedInterfaces(SpringProxy.class, Advised.class, DecoratingProxy.class))
                .registerJdkProxy(builder -> builder
                    .proxiedInterfaces(TypeReference.of("org.springframework.batch.core.explore.JobExplorer"))
                    .proxiedInterfaces(SpringProxy.class, Advised.class, DecoratingProxy.class));
            hints
                .reflection()
                .registerType(Types.class, MemberCategory.DECLARED_FIELDS);
            //.registerConstructor(Person.class.getDeclaredConstructor(), ExecutableMode.INVOKE)
            //.registerMethod(ReflectionUtils.findMethod(Person.class, "setName", String.class), ExecutableMode.INVOKE)
            //.registerMethod(ReflectionUtils.findMethod(Person.class, "getName"), ExecutableMode.INVOKE)
            //.registerMethod(ReflectionUtils.findMethod(Person.class, "setSurname", String.class), ExecutableMode.INVOKE)
            //.registerMethod(ReflectionUtils.findMethod(Person.class, "getSurname"), ExecutableMode.INVOKE);

            //hints.resources().registerPattern("sample-data.csv");*/
        }
    }
}
