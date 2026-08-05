@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello Jenkins Pipeline";
    }
}
