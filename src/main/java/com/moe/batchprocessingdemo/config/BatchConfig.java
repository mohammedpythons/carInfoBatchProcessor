package com.moe.batchprocessingdemo.config;

import com.moe.batchprocessingdemo.entity.Car;
import com.moe.batchprocessingdemo.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfig {


    private CarRepository carRepository;
    

    @Bean
    public FlatFileItemReader<Car> reader(){
        FlatFileItemReader<Car> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/cars_data.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Car> lineMapper() {
        DefaultLineMapper<Car> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "name", "model","dateOfCreation", "color", "price");

        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(new CarFieldSetMapper());

        return lineMapper;
    }
    @Bean
    public CarProcessor processor(){
        return new CarProcessor();
    }
    @Bean
    public RepositoryItemWriter<Car> writer(){
        RepositoryItemWriter<Car> writer = new RepositoryItemWriter<>();
        writer.setRepository(carRepository);
        writer.setMethodName("save");
        return writer;
    }
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("csv-step", jobRepository).<Car, Car>
                         chunk(100, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }
    @Bean
    public Job job1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("csv-job", jobRepository).start(step1(jobRepository, platformTransactionManager)).build();
    }
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
}
