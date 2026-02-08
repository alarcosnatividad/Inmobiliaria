package com.example.inmobiliaria.controladores;

import com.example.inmobiliaria.entidades.*;
import com.example.inmobiliaria.repositorios.CategoriaRepository;
import com.example.inmobiliaria.repositorios.MensajeRepository; // <--- FALTABA ESTE
import com.example.inmobiliaria.repositorios.PropiedadRepository;
import com.example.inmobiliaria.repositorios.RepositorioMeGusta;
import com.example.inmobiliaria.servicios.EmailService; // <--- FALTABA ESTE
import com.example.inmobiliaria.servicios.FileProcessingService;
import com.example.inmobiliaria.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // <--- FALTABA ESTE
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorPrincipal {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FileProcessingService fileProcessingService;

    @Autowired
    private RepositorioMeGusta repositorioMeGusta;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private EmailService emailService;

    // 1. PORTADA (INDEX)
    @GetMapping("/")
    // le voy a meter un objeto del tipo Model, al que se le pueden poner metodos como addAtribute
    // required=false, quiere decir que es opcional que me pase el id.. si no pues se muestran todas
    // mochila(los datos que pasare al html.. que les voy a dar nombre)
    public String index( Long categoriaId, Model mochila) {
        List<Propiedad> propiedades=propiedadRepository.findAll();

        mochila.addAttribute("listaPropiedades", propiedades);
        mochila.addAttribute("categorias", categoriaRepository.findAll());
        return "index";
    }

    @GetMapping("/categoria/{idCategoria}")
    public String listarCategoria(@PathVariable("idCategoria") Long idCategoria,Model model){
        Categoria categoria=categoriaRepository.findById(idCategoria).get();
        List<Propiedad>propiedades=propiedadRepository.findByCategoria(categoria);
        model.addAttribute("listaPropiedades", propiedades);

        model.addAttribute("categorias", categoriaRepository.findAll());
        return "index";

    }

    // 2. DETALLE DE UNA CASA
    @GetMapping("/propiedad/{id}") // id cambia , es una variable de ruta
    public String verDetalle(@PathVariable Long id, Model model, Authentication auth) {
        Propiedad propiedad = propiedadRepository.findById(id).orElse(null);
        if (propiedad == null) return "redirect:/";

        model.addAttribute("propiedad", propiedad); // ahora podemos usar en el html ${propiedad.titulo}

        // --- LÓGICA DE ME GUSTA ---
        int numeroLikes = repositorioMeGusta.countByPropiedad(propiedad); // cuenta el numero de filas y me las da
        model.addAttribute("numeroLikes", numeroLikes);

        boolean yaLeDiLike = false;
        if (auth != null) {
            Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
            List<MeGusta> misLikes = repositorioMeGusta.findByPropiedadAndUsuario(propiedad, usuario);
            if (!misLikes.isEmpty()) {
                yaLeDiLike = true;
            }
        }
        model.addAttribute("yaLeDiLike", yaLeDiLike);
        // ---------------------------

        return "detalle";
    }

    // 3. DAR LIKE
    @GetMapping("/propiedad/megusta/{id}")
    public String darLike(@PathVariable Long id, Authentication auth) {
        if (auth == null) return "redirect:/auth/login";

        Propiedad propiedad = propiedadRepository.findById(id).orElse(null);
        Usuario usuario = usuarioService.buscarPorEmail(auth.getName());

        if (propiedad != null && usuario != null) {
            List<MeGusta> likes = repositorioMeGusta.findByPropiedadAndUsuario(propiedad, usuario);

            if (!likes.isEmpty()) {
                repositorioMeGusta.deleteAll(likes);
            } else {
                MeGusta nuevoLike = new MeGusta();
                nuevoLike.setPropiedad(propiedad);
                nuevoLike.setUsuario(usuario);
                repositorioMeGusta.save(nuevoLike);
            }
        }
        return "redirect:/propiedad/" + id;
    }

    // 4. CARGAR FOTOS
    @GetMapping("/propiedades/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Resource recurso = fileProcessingService.downloadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

    // 5. MIS FAVORITOS
    @GetMapping("/favoritos")
    public String misFavoritos(Model model, Authentication auth) {
        if (auth == null) {
            return "redirect:/auth/login";
        }

        Usuario usuario = usuarioService.buscarPorEmail(auth.getName());
        List<MeGusta> likes = repositorioMeGusta.findByUsuario(usuario);
         // cada me gusta(sobre) con usuario y propiedad -- solo me interesa la propiedad
        List<Propiedad> misFavoritas = likes.stream()
                .map(MeGusta::getPropiedad)
                .collect(Collectors.toList());

        model.addAttribute("listaPropiedades", misFavoritas);
        return "favoritos";
    }

    // 6. ENVIAR MENSAJE (EMAIL + BASE DE DATOS) // lo coge del formulario de detalle
    @PostMapping("enviar-contacto")
    public String enviarMensaje(@RequestParam Long idPropiedad,
                                @RequestParam String nombre,
                                @RequestParam String telefono,
                                @RequestParam String mensaje) {
         // ahora coge el id para buscar toda la info de la casa
        Propiedad propiedad = propiedadRepository.findById(idPropiedad).orElse(null);

        if (propiedad != null) {
            // A) GUARDAR EN BASE DE DATOS
            // crea un nuevo mensaje y lo guarda en la bbdd
            Mensaje nuevoMensaje = new Mensaje(nombre, telefono, mensaje, propiedad);
            mensajeRepository.save(nuevoMensaje);

            // B) ENVIAR EMAIL AL ADMINISTRADOR
            String asunto = "🏠 Nuevo interés en: " + propiedad.getTitulo();
            String cuerpo = "Hola Admin,\n\n" +
                    "Alguien está interesado en la propiedad (ID: " + idPropiedad + ")\n" +
                    "--------------------------------------------------\n" +
                    "👤 Nombre: " + nombre + "\n" +
                    "📞 Teléfono: " + telefono + "\n" +
                    "💬 Mensaje: " + mensaje + "\n" +
                    "--------------------------------------------------\n" +
                    "Revisa el panel de administración.";

            // ENVÍO DE CORREO
            emailService.enviarCorreo("alarcosgalveznatividad@gmail.com", asunto, cuerpo);
        }

        return "redirect:/propiedad/" + idPropiedad + "?exito=true";
    }

    // 7. VER BUZÓN DE MENSAJES (PROTEGIDO)
    @GetMapping("/mensajes")
    public String verMensajes(Model model, Authentication auth) {
        // 1. Si no está logueado -> Login
        if (auth == null) return "redirect:/auth/login";

        // 2. SEGURIDAD EXTRA: ¿Es el administrador?
        // CAMBIA ESTE CORREO POR EL TUYO EXACTO
        String emailAdmin = "alarcosgalveznatividad@gmail.com";

        if (!auth.getName().equals(emailAdmin)) {
            return "redirect:/"; // Si no es el admin, lo mandamos al inicio
        }

        // 3. Si pasa el control, le enseñamos los mensajes
        List<Mensaje> listaMensajes = mensajeRepository.findAll();
        model.addAttribute("mensajes", listaMensajes);

        return "mensajes";
    }

    // 2. ver perfil
    @GetMapping("/perfil/usuario/{id}") // id cambia , es una variable de ruta
    public String verPerfil(@PathVariable Long id, Model model, Authentication auth) {
        List<Usuario> listaperfil;
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return "redirect:/";

        model.addAttribute("perfil", usuario); // ahora podemos usar en el html ${perfil.nombre}
        return "perfil";
    }
}