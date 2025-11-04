package assignment.karina.weather.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "assignment.karina.weather")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
