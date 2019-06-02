package hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"hotel"})
public class HotelApiApplication {

    public HotelApiApplication() {
        // Spring boot start app
    }

    public static void main(String[] args) {
        SpringApplication.run(HotelApiApplication.class, args);
    }
}
