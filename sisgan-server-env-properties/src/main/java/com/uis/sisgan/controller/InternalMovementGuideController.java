package com.uis.sisgan.controller;

import java.util.List;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import com.uis.sisgan.persistence.entity.InternalMovementGuide;
import com.uis.sisgan.persistence.entity.Propietary;
import com.uis.sisgan.persistence.entity.Transporter;
import com.uis.sisgan.service.InternalMovementGuideService;
import com.uis.sisgan.service.PropietaryService;
import com.uis.sisgan.service.TransporterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/internal-movement-guide")
public class InternalMovementGuideController {

    @Autowired
    private PropietaryService propietaryService;

    @Autowired
    private TransporterService transporterService;

    @Autowired
    private InternalMovementGuideService internalMovementGuideService;

    /**
     * Busca todas las guías de movimiento interno que tenga el usuario. Este
     * usuario debe ser un transportador, porque si no lo es entonces se
     * retornará un código de estado {@code 404}.
     *
     * El usuario debe mandar sus credenciales de autenticación.
     *
     * @param principal Objeto con las credenciales del usuario.
     * @return Si se encuentran guías de movimiento interno se devolverán en una
     *         lista, aún así no se tenga ninguna.
     */
    @GetMapping()
    public ResponseEntity<List<InternalMovementGuide>> getAll(Principal principal) {
        ArrayList<InternalMovementGuide> guides = null;
        ResponseEntity<List<InternalMovementGuide>> response;

        try {
            guides = new ArrayList<>(internalMovementGuideService.getAllByTransporter(principal.getName()));
            response = ResponseEntity.ok(guides);
        } catch (UserPrincipalNotFoundException upnfEx) {
            response = ResponseEntity.badRequest().build();
        }

        return response;
    }

    /**
     * Guarda una guía de movimiento interno.
     *
     * El usuario que quiera guardar una guía de movimiento debe estar
     * autenticado y ser un propietario; además, el usuario que seleccione como
     * transportador será validado si realmente lo es.
     *
     * @param internalMovementGuide Datos de la guía de movimiento interno a
     *                              guardar.
     * @param ucb                   Objeto con los datos de la URL.
     * @param principal             Objeto con los datos y credenciales de
     *                              usuario.
     * @return Si las validaciones fueron exitosas entonces se retornan los
     *         datos de la guía de movimiento interno que fueron guardados en
     *         BD; en caso contrario se obtendrá un código de estado {@code 403}
     *         (no es propietario), o un {@code 404} (no se encontró el
     *         transportador).
     */
    @PostMapping()
    public ResponseEntity<InternalMovementGuide> save(@RequestBody InternalMovementGuide internalMovementGuide, UriComponentsBuilder ucb, Principal principal) {
        ResponseEntity<InternalMovementGuide> res;
        Propietary propietary;
        Transporter transporter;
        propietary = propietaryService.findByEmail(principal.getName());
        if (propietary == null) {
            res = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            return res;
        }

        transporter = transporterService.findById(internalMovementGuide.getTransporter().getId());
        if (transporter == null) {
            res = ResponseEntity.notFound().build();
            return res;
        }

        internalMovementGuide.setTransporter(transporter);
        var savedIntMovementGuide = internalMovementGuideService.save(internalMovementGuide);

        URI location = ucb
                .path("/internal-movement-guide/" + savedIntMovementGuide.getId())
                .buildAndExpand(savedIntMovementGuide.getId())
                .toUri();

        res = ResponseEntity.created(location).body(savedIntMovementGuide);
        return res;
    }

    @PatchMapping
    public ResponseEntity<InternalMovementGuide> update(@RequestBody InternalMovementGuide internalMovementGuide, Principal principal) {
        int amountFields = internalMovementGuide.getClass().getDeclaredFields().length;
        InternalMovementGuide original;
        InternalMovementGuide updated;

        if (internalMovementGuide.getId() != null) {
            original = internalMovementGuideService.findById(internalMovementGuide.getId());
        } else {
            return ResponseEntity.badRequest().build();
        }
        // [WIP]
        updated = internalMovementGuideService.update(internalMovementGuide);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<List<InternalMovementGuide>> delete(@RequestBody List<InternalMovementGuide> internalMovementGuides, Principal principal) {
        boolean isDeleted = false;
        ArrayList<Integer> ids = new ArrayList<>();

        for (InternalMovementGuide guide : internalMovementGuides) {
            ids.add(guide.getId());
        }

        isDeleted = internalMovementGuideService.deleteAllById(ids);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}