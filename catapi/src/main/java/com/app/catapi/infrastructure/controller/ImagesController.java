package com.app.catapi.infrastructure.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
@AllArgsConstructor
@Slf4j
public class ImagesController {
    @GetMapping("/search")
    public ResponseEntity<List<BreedDto>> getImagesByBreedId(@RequestParam String breedId){
        log.info("Getting images by breed id {}", breedId);

        if (breedId == null || breedId.isEmpty()) {
            log.warn("Invalid breed id");
            return ResponseEntity.notFound().build();
        }}
}
