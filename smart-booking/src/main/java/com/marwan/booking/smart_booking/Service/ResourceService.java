package com.marwan.booking.smart_booking.Service;

import com.marwan.booking.smart_booking.Dto.ResourceRequest;
import com.marwan.booking.smart_booking.Entity.Resource;
import com.marwan.booking.smart_booking.Entity.User;
import com.marwan.booking.smart_booking.Repository.ResourceRepository;
import com.marwan.booking.smart_booking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public Resource addResource(ResourceRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User owner = userRepository.findById(userDetails.getId()).orElseThrow();

        Resource resource = Resource.builder()
                .name(request.getName())
                .type(request.getType())
                .owner(owner)
                .build();

        return resourceRepository.save(resource);
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public List<Resource> searchResources(String name, String type) {
        return resourceRepository.searchResources(name, type);
    }

}
