package dev.joseluisgs.tiendaapispringboot.web.localizacion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping("/locale")
public class LocalizacionWebController {

    private final MessageSource messageSource;

    @Autowired
    public LocalizacionWebController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    public String welcome(Model model, Locale locale) {
        String welcomeMessage = messageSource.getMessage("welcome.message", null, locale);
        LocalDate date = LocalDate.now();
        model.addAttribute("welcomeMessage", welcomeMessage);
        // Obtener la fecha localizada en función del Locale
        String localizedDate = getLocalizedDate(locale);
        model.addAttribute("date", localizedDate);
        model.addAttribute("locale", locale);
        return "locale/index";
    }

    private String getLocalizedDate(Locale locale) {
        // Obtener la fecha actual
        Date currentDate = new Date();

        // Formatear la fecha localizada en función del Locale
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
        return dateFormat.format(currentDate);
    }
}