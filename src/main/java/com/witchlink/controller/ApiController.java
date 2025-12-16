package com.witchlink.controller;

import com.witchlink.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    
    private final LinkService linkService;
    
    @PostMapping("/links/{id}/click")
    public void trackClick(@PathVariable Long id) {
        linkService.incrementClickCount(id);
    }
}
