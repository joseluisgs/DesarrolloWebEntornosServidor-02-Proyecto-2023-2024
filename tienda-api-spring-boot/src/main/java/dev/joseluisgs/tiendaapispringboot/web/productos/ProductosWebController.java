package dev.joseluisgs.tiendaapispringboot.web.productos;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.services.ProductosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/productos")
@Slf4j
public class ProductosWebController {
    private final ProductosService productosService;

    @Autowired
    public ProductosWebController(ProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping
    public String login() {
        log.info("Login GET");
        return "productos/login";
    }

    @PostMapping
    public String login(@RequestParam("password") String password) {
        log.info("Login POST");
        if ("pass".equals(password)) {
            return "redirect:/productos/index";
        } else {
            return "productos/login";
        }
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(value = "search", required = false) Optional<String> search,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Index GET con parámetros search: " + search + ", page: " + page + ", size: " + size);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        Pageable pageable = PageRequest.of(page, size, sort);
        // Obtenemos la página de productos
        var productosPage = productosService.findAll(search, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);
        model.addAttribute("productosPage", productosPage);
        model.addAttribute("search", search.orElse(""));
        return "productos/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Producto producto = productosService.findById(id);
        model.addAttribute("producto", producto);
        return "productos/details";
    }

    /*
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("producto", new ProductoCreateDto());
        return "create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("producto") ProductoCreateDto productoDto, BindingResult result) {
        if (result.hasErrors()) {
            return "create";
        }
        productoService.createProducto(productoDto);
        return "redirect:/productos/index";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        Producto producto = productoService.getProductoById(id);
        model.addAttribute("producto", producto);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("producto") ProductoCreateDto productoDto,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "update";
        }
        productoService.updateProducto(id, productoDto);
        return "redirect:/productos/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productoService.deleteProducto(id);
        return "redirect:/productos/index";
    }*/
}