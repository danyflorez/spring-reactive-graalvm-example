package com.demo.customer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}

@Controller
@ResponseBody
record CustomerRestController(CustomerRepository repository) {

    @GetMapping("/customers")
    Flux<Customer> get(){
        return this.repository.findAll();
    }
}


@Component
record CostumerRunner(CustomerRepository customerRepository) implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var names = Flux.just("Daniel", "Karen", "Camilo", "Nubia",
                                            "Luis Miguel", "Andres", "Felipe", "Laura")
                .map(name -> new Customer(null, name))
                .flatMap(this.customerRepository::save);

        names.subscribe(System.out::println);
    }
}

interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}

record Customer(@Id Integer id, String name) {
}