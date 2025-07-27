package com.marwan.booking.smart_booking.Controller;

import com.marwan.booking.smart_booking.Dto.ResourceRequest;
import com.marwan.booking.smart_booking.Entity.Resource;
import com.marwan.booking.smart_booking.Service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<Resource> createResource(@RequestBody ResourceRequest request) {
        return ResponseEntity.ok(resourceService.addResource(request));
    }

    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Resource>> searchResources(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type
    ) {
        return ResponseEntity.ok(resourceService.searchResources(name, type));
    }

}
