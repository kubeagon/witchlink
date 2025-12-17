package com.witchlink.controller;

import com.witchlink.model.Link;
import com.witchlink.model.User;
import com.witchlink.service.LinkService;
import com.witchlink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final UserService userService;
    private final LinkService linkService;
    
    @GetMapping
    public String adminDashboard(Model model) {
        List<User> users = userService.findAll();
        List<Link> allLinks = linkService.findAll();
        
        // Calculate statistics
        long totalUsers = users.size();
        long activeUsers = users.stream().filter(User::getIsActive).count();
        long totalLinks = allLinks.size();
        long totalClicks = allLinks.stream().mapToLong(Link::getClickCount).sum();
        long activeLinks = allLinks.stream().filter(Link::getIsActive).count();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("totalLinks", totalLinks);
        model.addAttribute("totalClicks", totalClicks);
        model.addAttribute("activeLinks", activeLinks);
        model.addAttribute("recentUsers", users.stream()
                .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
                .limit(10)
                .toList());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }
    
    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Link> userLinks = linkService.findByUserId(id);
        
        long totalClicks = userLinks.stream().mapToLong(Link::getClickCount).sum();
        long activeLinks = userLinks.stream().filter(Link::getIsActive).count();
        
        model.addAttribute("user", user);
        model.addAttribute("links", userLinks);
        model.addAttribute("totalClicks", totalClicks);
        model.addAttribute("activeLinks", activeLinks);
        
        return "admin/user-detail";
    }
    
    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(!user.getIsActive());
        userService.save(user);
        
        redirectAttributes.addFlashAttribute("message", 
                "User " + user.getUsername() + " has been " + (user.getIsActive() ? "activated" : "deactivated"));
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{id}/toggle-admin")
    public String toggleAdminRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if ("ADMIN".equals(user.getRole())) {
            user.setRole("USER");
        } else {
            user.setRole("ADMIN");
        }
        userService.save(user);
        
        redirectAttributes.addFlashAttribute("message", 
                "User " + user.getUsername() + " role updated to " + user.getRole());
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String username = user.getUsername();
        userService.deleteById(id);
        
        redirectAttributes.addFlashAttribute("message", "User " + username + " has been deleted");
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/links/{id}/delete")
    public String deleteLink(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Link link = linkService.findById(id)
                .orElseThrow(() -> new RuntimeException("Link not found"));
        Long userId = link.getUser().getId();
        linkService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Link has been deleted");
        return "redirect:/admin/users/" + userId;
    }
}
