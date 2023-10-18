package dev.joseluisgs.tiendaapispringboot.storage.config;

import dev.joseluisgs.tiendaapispringboot.storage.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StorageConfig {
    @Autowired
    private StorageService storageService;

    @Value("${upload.delete}")
    private String deleteAll;

    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            storageService.deleteAll();
        }

        storageService.init(); // inicializamos
    }
}