package lk.ijse.gdse.traditionalexperiencebackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TraditionalExperienceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TraditionalExperienceBackendApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
