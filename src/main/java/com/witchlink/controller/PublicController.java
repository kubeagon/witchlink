package com.witchlink.controller;

import com.witchlink.model.User;
import com.witchlink.service.LinkService;
import com.witchlink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PublicController {
    
    private final UserService userService;
    private final LinkService linkService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/u/{username}")
    public String userProfile(@PathVariable String username, Model model) {
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return "404";
        }
        
        User user = userOpt.get();
        if (!user.getIsActive()) {
            return "404";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("links", linkService.findActiveLinksByUser(user));
        
        return "profile";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
