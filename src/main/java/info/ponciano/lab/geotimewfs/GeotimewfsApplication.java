/*
 * Copyright (C)  2021 Dr Claire Prudhomme <claire@prudhomme.info).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.geotimewfs;

import info.ponciano.lab.geotimewfs.controllers.storage.StorageProperties;
import info.ponciano.lab.geotimewfs.controllers.storage.StorageService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *public class of GeotimewfsApplication
 * 
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@RestController
public class GeotimewfsApplication {

    /**
     * Management of OAuth2 authentication
     *
     * @param principal Oauth2 user
     * @return user attributes as MAP.
     */
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> map = new HashMap<>();

        if (principal != null) {
            map.put("name", principal.getAttribute("name"));
        }
        return map;

    }

    /**
     *Public access point of GeotimewfsApplication
     * @param args  Specicific args properties     
     */
    public static void main(String[] args) {
        SpringApplication.run(GeotimewfsApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    
}
