package com.uis.sisgan.controller;

import com.uis.sisgan.persistence.entity.IcaOfficial;
import com.uis.sisgan.service.IcaOfficialService;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/ica-official")
public class IcaOfficialController {
    
    @Autowired
    private IcaOfficialService icaOfficialService;
    
    @GetMapping()
    public ResponseEntity<IcaOfficial> getInfo(Principal principal) {
        IcaOfficial icaOfficial = icaOfficialService.findByEmail(principal.getName());
        
        if (icaOfficial == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(icaOfficial);
        }
    }
    
    @PostMapping()
    public ResponseEntity<IcaOfficial> register(@RequestBody IcaOfficial icaOfficial, UriComponentsBuilder ucb) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPss = encoder.encode(icaOfficial.getPassword());
        icaOfficial.setPassword(encodedPss);
        IcaOfficial savedIcaOfficial = icaOfficialService.save(icaOfficial);
        
        URI location = ucb
            .path("ica-official/" + savedIcaOfficial.getId())
            .buildAndExpand(savedIcaOfficial.getId())
            .toUri();
        
        ResponseEntity<IcaOfficial> res = ResponseEntity.created(location).body(savedIcaOfficial);
        return res;
    }
}