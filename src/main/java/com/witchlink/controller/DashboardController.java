package com.witchlink.controller;

import com.witchlink.model.Link;
import com.witchlink.model.User;
import com.witchlink.service.LinkService;
import com.witchlink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final UserService userService;
    private final LinkService linkService;
    
    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Link> links = linkService.findLinksByUser(user);
        long activeLinksCount = links.stream().filter(Link::getIsActive).count();
        long totalClicks = links.stream().mapToLong(Link::getClickCount).sum();
        
        model.addAttribute("user", user);
        model.addAttribute("links", links);
        model.addAttribute("activeLinksCount", activeLinksCount);
        model.addAttribute("totalClicks", totalClicks);
        model.addAttribute("profileUrl", "/u/" + user.getUsername());
        
        return "dashboard";
    }
    
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "profile-edit";
    }
    
    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                               @RequestParam String displayName,
                               @RequestParam String bio,
                               @RequestParam String theme,
                               RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        user.setDisplayName(displayName);
        user.setBio(bio);
        user.setTheme(theme);
        
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/dashboard/profile";
    }
    
    @GetMapping("/links/new")
    public String newLink() {
        return "link-form";
    }
    
    @PostMapping("/links")
    public String createLink(Authentication authentication,
                            @RequestParam String title,
                            @RequestParam String url,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String icon,
                            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        Link link = new Link();
        link.setTitle(title);
        link.setUrl(url);
        link.setDescription(description);
        link.setIcon(icon);
        link.setUser(user);
        
        linkService.createLink(link);
        
        redirectAttributes.addFlashAttribute("success", "Link created successfully!");
        return "redirect:/dashboard";
    }
    
    @GetMapping("/links/{id}/edit")
    public String editLink(@PathVariable Long id, Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Link> linkOpt = linkService.findById(id);
        if (linkOpt.isEmpty() || !linkOpt.get().getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("link", linkOpt.get());
        return "link-edit";
    }
    
    @PostMapping("/links/{id}")
    public String updateLink(@PathVariable Long id,
                            Authentication authentication,
                            @RequestParam String title,
                            @RequestParam String url,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String icon,
                            @RequestParam Boolean isActive,
                            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Link> linkOpt = linkService.findById(id);
        if (linkOpt.isEmpty() || !linkOpt.get().getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }
        
        Link link = linkOpt.get();
        link.setTitle(title);
        link.setUrl(url);
        link.setDescription(description);
        link.setIcon(icon);
        link.setIsActive(isActive);
        
        linkService.updateLink(link);
        
        redirectAttributes.addFlashAttribute("success", "Link updated successfully!");
        return "redirect:/dashboard";
    }
    
    @PostMapping("/links/{id}/delete")
    public String deleteLink(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Link> linkOpt = linkService.findById(id);
        if (linkOpt.isPresent() && linkOpt.get().getUser().getId().equals(user.getId())) {
            linkService.deleteLink(id);
            redirectAttributes.addFlashAttribute("success", "Link deleted successfully!");
        }
        
        return "redirect:/dashboard";
    }
    
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();
        return userService.findByUsername(username).orElse(null);
    }
}
