package com.uis.sisgan.security;

import com.uis.sisgan.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Este servicio de seguridad proporciona la búsqueda de la información de un 
 * usuario, necesaria para los procesos de autenticación.
 * 
 * El "username" se refiere al correo electrónico que cada usuario tiene.
 * 
 * @author Anderson Acuña
 */
public class UserServiceSecurity implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Busca un usuario que tenga el correo electrónico deseado.
     * 
     * Este método solo se usa con propósitos de seguridad de la aplicación.
     * 
     * @param email Dirección de correo electrónico.
     * @return El usuario encapsulado en una clase que implementa {@link UserDetails}.
     * @throws UsernameNotFoundException Si no hay usuario con ese correo.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        
        if(user.isPresent()) {
            return new SecurityUser(user.get());
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }
    
}
