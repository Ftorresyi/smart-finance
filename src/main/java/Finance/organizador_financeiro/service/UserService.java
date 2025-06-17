package Finance.organizador_financeiro.service;

import Finance.organizador_financeiro.mapper.UserMapper;
import Finance.organizador_financeiro.repository.UserRepository;
import Finance.organizador_financeiro.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
