package com.uis.sisgan.controller;

import com.uis.sisgan.persistence.PropietaryRepository;
import com.uis.sisgan.persistence.UserRepository;
import com.uis.sisgan.persistence.entity.Cattle;
import com.uis.sisgan.persistence.entity.Propietary;
import com.uis.sisgan.persistence.entity.User;
import com.uis.sisgan.service.CattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cattle")
public class CattleController {

    @Autowired
    CattleService cattleService;

    @Autowired
    PropietaryRepository propietaryRepository;

    @GetMapping()
    public ResponseEntity<List<Cattle>> getCattlesByEmail(@RequestHeader("Authorization") String authorizationHeader){
        String email = extractEmailFromAuthorizationHeader(authorizationHeader);
        return cattleService.getCattlesByEmail(email).map(
                        cattles -> new ResponseEntity<>(cattles, HttpStatus.OK)).
                orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cattle>> getAllCatlles(){
        return new ResponseEntity<>(cattleService.getAll(), HttpStatus.OK);
    }



    @PostMapping("/save")
    public ResponseEntity<Cattle> save(@RequestBody Cattle cattle){
        return new ResponseEntity<>(cattleService.save(cattle), HttpStatus.CREATED);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity patch(@PathVariable("id") Integer id , @RequestBody Cattle cattle){
        return new ResponseEntity<>(cattleService.patchCattle(id,cattle), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id){
            if (cattleService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

    private String extractEmailFromAuthorizationHeader(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes);
        String[] values = credentials.split(":", 2);
        return values[0]; // This is the email (username)
    }

}
