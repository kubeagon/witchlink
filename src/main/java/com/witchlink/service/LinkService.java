package com.witchlink.service;

import com.witchlink.model.Link;
import com.witchlink.model.User;
import com.witchlink.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {
    
    private final LinkRepository linkRepository;
    
    @Transactional
    public Link createLink(Link link) {
        link.setCreatedAt(LocalDateTime.now());
        link.setUpdatedAt(LocalDateTime.now());
        return linkRepository.save(link);
    }
    
    public List<Link> findLinksByUser(User user) {
        return linkRepository.findByUserOrderByDisplayOrderAsc(user);
    }
    
    public List<Link> findActiveLinksByUser(User user) {
        return linkRepository.findByUserAndIsActiveOrderByDisplayOrderAsc(user, true);
    }
    
    public Optional<Link> findById(Long id) {
        return linkRepository.findById(id);
    }
    
    @Transactional
    public Link updateLink(Link link) {
        link.setUpdatedAt(LocalDateTime.now());
        return linkRepository.save(link);
    }
    
    @Transactional
    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteById(Long id) {
        linkRepository.deleteById(id);
    }
    
    public List<Link> findAll() {
        return linkRepository.findAll();
    }
    
    public List<Link> findByUserId(Long userId) {
        return linkRepository.findAll().stream()
                .filter(link -> link.getUser().getId().equals(userId))
                .toList();
    }
    
    @Transactional
    public void incrementClickCount(Long linkId) {
        Optional<Link> linkOpt = linkRepository.findById(linkId);
        if (linkOpt.isPresent()) {
            Link link = linkOpt.get();
            link.setClickCount(link.getClickCount() + 1);
            linkRepository.save(link);
        }
    }
}
