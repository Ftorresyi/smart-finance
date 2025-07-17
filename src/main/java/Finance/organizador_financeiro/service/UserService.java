package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.domain.User;
import Finance.organizador_financeiro.dto.RegisterDTO;
import Finance.organizador_financeiro.dto.UserDTO;
import Finance.organizador_financeiro.mapper.UserMapper;
import Finance.organizador_financeiro.repository.UserRepository;
import Finance.organizador_financeiro.security.AuthenticationComponent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationComponent authenticationComponent;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder, AuthenticationComponent authenticationComponent) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationComponent = authenticationComponent;
    }

    public void registerNewUser(RegisterDTO registerDTO) {
        if (repository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered.");
        }

        User newUser = User.builder()
                .name(registerDTO.getName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        repository.save(newUser);
    }

    @Transactional(readOnly = true)
    public UserDTO findAuthenticatedUserDTO() {
        User user = authenticationComponent.getAuthenticatedUser();
        return mapper.toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User authenticatedUser = authenticationComponent.getAuthenticatedUser();

        // Check if the new email is already in use by another user
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(authenticatedUser.getEmail())) {
            if (repository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new IllegalStateException("Email already in use by another user.");
            }
        }

        // Update user fields
        if (userDTO.getName() != null) {
            authenticatedUser.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            authenticatedUser.setEmail(userDTO.getEmail());
        }

        User updatedUser = repository.save(authenticatedUser);
        return mapper.toDTO(updatedUser);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        User authenticatedUser = authenticationComponent.getAuthenticatedUser();

        if (!passwordEncoder.matches(oldPassword, authenticatedUser.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password.");
        }

        authenticatedUser.setPassword(passwordEncoder.encode(newPassword));
        repository.save(authenticatedUser);
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
