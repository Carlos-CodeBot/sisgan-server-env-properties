package com.uis.sisgan.controller;

import com.uis.sisgan.persistence.entity.Transporter;
import com.uis.sisgan.service.TransporterService;
import java.net.URI;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Anderson Acuña
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/transporter")
public class TransporterController {
    
    @Autowired
    private TransporterService transporterService;
    
    @GetMapping()
    public ResponseEntity<Transporter> getInfo(Principal principal) {
        Transporter transporter = transporterService.findByEmail(principal.getName());
        
        if (transporter == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transporter);
        }
    }
    
    @PostMapping()
    public ResponseEntity<Transporter> register(@RequestBody Transporter transporter, UriComponentsBuilder ucb) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPss = encoder.encode(transporter.getPassword());
        transporter.setPassword(encodedPss);
        Transporter savedTransporter = transporterService.save(transporter);
        
        URI location = ucb
            .path("transporter/" + savedTransporter.getId())
            .buildAndExpand(savedTransporter.getId())
            .toUri();
        
        ResponseEntity<Transporter> res = ResponseEntity.created(location).body(savedTransporter);
        return res;
    }
}