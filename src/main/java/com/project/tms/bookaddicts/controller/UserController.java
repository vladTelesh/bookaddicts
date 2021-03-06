package com.project.tms.bookaddicts.controller;

import com.project.tms.bookaddicts.pojo.User;
import com.project.tms.bookaddicts.service.SecurityService;
import com.project.tms.bookaddicts.validator.PasswordValidator;
import com.project.tms.bookaddicts.validator.entity.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.project.tms.bookaddicts.service.UserService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PasswordValidator passwordValidator;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        passwordValidator.validate(userForm,bindingResult);

        if (bindingResult.hasErrors()) {

            return "registration";
        }
        userService.save(userForm);
        securityService.autoLogin(userForm.getEmail(), userForm.getPasswordConfirm());

        return "redirect:/home";
    }

    @GetMapping("/index")
    public String login(Model model, String error) {

        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");

        }
        return "index";
    }


    @GetMapping("/user-setting")
    public String userSetting(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user-settings";
    }

    @PostMapping("/user-setting")
    public String userEdit(@ModelAttribute User user, Model model, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user-settings";
        }
        userService.update(user);
        model.addAttribute("message", "SUCCESS");
        return "user-settings";
    }
}
