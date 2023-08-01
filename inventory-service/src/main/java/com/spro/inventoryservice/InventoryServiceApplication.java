package com.spro.inventoryservice;

import com.spro.inventoryservice.model.Inventory;
import com.spro.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
/*			Inventory inventory1 = new Inventory(
					"iphone13_aabb",
					89
			);
			Inventory inventory2 = new Inventory(
					"A30_abb",
					54
			);
			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);*/

		};
	}
}
