package dev.joseluisgs.tiendaapispringboot.web.productos;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.services.CategoriasService;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.services.ProductosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/productos")
@Slf4j
public class ProductosWebController {
    private final ProductosService productosService;
    private final CategoriasService categoriasService;

    @Autowired
    public ProductosWebController(ProductosService productosService, CategoriasService categoriasService) {
        this.productosService = productosService;
        this.categoriasService = categoriasService;
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

    @GetMapping("/create")
    public String createForm(Model model) {
        log.info("Create GET");
        var categorias = categoriasService.findAll(Optional.empty(), Optional.empty(), PageRequest.of(0, 1000))
                .get()
                .map(Categoria::getNombre);
        var producto = ProductoCreateDto.builder()
                .imagen("https://via.placeholder.com/150")
                .precio(0.0)
                .stock(0)
                .build();
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categorias);
        return "productos/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("producto") ProductoCreateDto productoDto,
                         BindingResult result,
                         Model model) {
        log.info("Create POST");
        if (result.hasErrors()) {
            var categorias = categoriasService.findAll(Optional.empty(), Optional.empty(), PageRequest.of(0, 1000))
                    .get()
                    .map(Categoria::getNombre);
            model.addAttribute("categorias", categorias);
            return "productos/create";
        }
        // Salvamos el producto
        var producto = productosService.save(productoDto);
        return "redirect:/productos/index";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        var categorias = categoriasService.findAll(Optional.empty(), Optional.empty(), PageRequest.of(0, 1000))
                .get()
                .map(Categoria::getNombre);
        Producto producto = productosService.findById(id);
        ProductoUpdateDto productoUpdateDto = ProductoUpdateDto.builder()
                .marca(producto.getMarca())
                .modelo(producto.getModelo())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .imagen(producto.getImagen())
                .categoria(producto.getCategoria().getNombre())
                .stock(producto.getStock())
                .isDeleted(producto.getIsDeleted())
                .build();
        model.addAttribute("producto", productoUpdateDto);
        model.addAttribute("categorias", categorias);
        return "productos/update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute ProductoUpdateDto productoUpdateDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            var categorias = categoriasService.findAll(Optional.empty(), Optional.empty(), PageRequest.of(0, 1000))
                    .get()
                    .map(Categoria::getNombre);
            model.addAttribute("categorias", categorias);
            return "productos/update";
        }
        log.info("Update POST");
        System.out.println(id);
        System.out.println(productoUpdateDto);
        var res = productosService.update(id, productoUpdateDto);
        System.out.println(res);
        return "redirect:/productos/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productosService.deleteById(id);
        return "redirect:/productos/index";
    }

    @GetMapping("/update-image/{id}")
    public String showUpdateImageForm(@PathVariable("id") Long productId, Model model) {
        Producto producto = productosService.findById(productId);
        model.addAttribute("producto", producto);
        return "productos/update-image";
    }

    @PostMapping("/update-image/{id}")
    public String updateProductImage(@PathVariable("id") Long productId, @RequestParam("imagen") MultipartFile imagen) {
        log.info("Update POST con imagen");
        productosService.updateImage(productId, imagen, true);
        return "redirect:/productos/index";
    }

}