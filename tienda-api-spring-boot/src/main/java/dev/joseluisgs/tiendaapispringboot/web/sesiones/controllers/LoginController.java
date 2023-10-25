package dev.joseluisgs.tiendaapispringboot.web.sesiones.controllers;

import dev.joseluisgs.tiendaapispringboot.web.sesiones.store.UserStore;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/sesion")
public class LoginController {
    private final UserStore userSession;

    @Autowired
    public LoginController(UserStore userSession) {
        this.userSession = userSession;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        log.info("login");
        return "/sesion/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("password") String password, HttpSession session, Model model) {
        log.info("Login {}", password);
        if ("pass".equals(password)) {
            // Si es correcto, creamos la sesión
            userSession.incrementLoginCount();
            session.setAttribute("userSession", userSession);
            // Establece el tiempo de caducidad de la sesión en 1800 segundos (30 minutos)
            session.setMaxInactiveInterval(1800);
            return "redirect:/sesion/dashboard";
        } else {
            model.addAttribute("error", "Invalid password");
            return "sesion/login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        log.info("Dashboard");
        UserStore sessionData = (UserStore) session.getAttribute("userSession");
        model.addAttribute("loginCount", sessionData.getLoginCount());
        return "sesion/dashboard";
    }
}