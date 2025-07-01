package Finance.organizador_financeiro.controller;

import Finance.organizador_financeiro.dto.LoginDTO;
import Finance.organizador_financeiro.dto.LoginResponseDTO;
import Finance.organizador_financeiro.dto.RegisterDTO;
import Finance.organizador_financeiro.service.TokenService;
import Finance.organizador_financeiro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.registerNewUser(registerDTO);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // 1. Cria um objeto de autenticação com as credenciais do usuário
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        // 2. O Spring Security usa o AuthenticationManager para autenticar o usuário
        // (ele chamará nosso AuthenticationService por baixo dos panos)
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação for bem-sucedida, gera o token JWT
        String token = tokenService.generateToken(auth);

        // 4. Retorna o token no corpo da resposta
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
