package com.company.intecap.address.controllers;

import com.company.intecap.address.models.entity.Cliente;
import com.company.intecap.address.models.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    //@Autowired
    //private IClienteService clienteService;

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @RequestMapping(value = "/prueba", method = RequestMethod.GET)
    public String prueba(Model model) {

        Cliente c1 = new Cliente();
        c1.setNombre("Pedro Lopez");
        c1.setDireccion1("8va Calle 31-74");
        c1.setDireccion2("zona 4");
        c1.setCiudad("Mixco");
        c1.setDepartamento("Guatemala");
        c1.setCodigoPostal("01057");

        Cliente c2 = new Cliente();
        c2.setNombre("Juan Perez");
        c2.setDireccion1("7 av. 12-50 zona 7");
        c2.setDireccion2("zona 4");
        c2.setCiudad("Guatemala");
        c2.setDepartamento("Guatemala");
        c2.setCodigoPostal("01007");

        List<Cliente> clientes = Arrays.asList(c1, c2);

        model.addAttribute("clientes", clientes);
        model.addAttribute("titulo", "Listado de Clientes");

        return "prueba";

    }

    // Controlador para mostrar listar los datos.

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de Clientes");
        model.addAttribute("clientes", clienteService.findAll());

        return "listar";
    }

    // cargar la data al formulario
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();

        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Clientes");
        return "form";
    }

    // controlador crear un nuevo Registro
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        try{
            clienteService.save(cliente);
            status.setComplete();
            return "redirect:listar";
        }catch (DataIntegrityViolationException e){
            result.rejectValue("nombre","error.cliente","Ya existe un cliente con ese nombre");
            model.addAttribute("titulo","Formulario de cliente");
            return "form";
        }
    }

    //Actualizar un Registro
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model){
        Cliente cliente = null;

        if(id>0){ // seria una actualización
            cliente = clienteService.findOne(id);
        }else{
            return "redirect:/listar";
        }
        model.put("cliente",cliente);
        model.put("titutlo","Editar Cliente");

        return "form";
    }

    // Eliminar un registro
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id){
        if(id>0){
            clienteService.delete(id);
        }
        return "redirect:/listar";
    }

}
