package dev.joseluisgs.tiendaapispringboot.web.welcome.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Es del tipo Controller
// Model es el modelo que se le pasa a la vista
@Controller
public class WelcomeWebController {
    @GetMapping("/welcome")
    public String welcome(Model model) {
        // Añadimos un atributo al modelo, que se podrá usar en la vista
        model.addAttribute("name", "2º DAW");
        // Devolvemos la vista (welcome.html)
        return "welcome";
    }
}
