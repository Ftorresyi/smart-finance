package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.RegisterDTO;
import Finance.organizador_financeiro.dto.UserDTO;
import Finance.organizador_financeiro.mapper.UserMapper;
import Finance.organizador_financeiro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(RegisterDTO registerDTO) {
        if (repository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("E-mail já cadastrado.");
        }

        User newUser = User.builder()
                .name(registerDTO.getName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        repository.save(newUser);
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }
}