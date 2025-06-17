package Finance.organizador_financeiro.service;


import Finance.organizador_financeiro.domain.TransactionType;
import Finance.organizador_financeiro.dto.TransactionTypeDTO;
import Finance.organizador_financeiro.mapper.TransactionTypeMapper;
import Finance.organizador_financeiro.repository.TransactionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class TransactionTypeService {

    private final TransactionTypeRepository repository;
    public final TransactionTypeMapper mapper;

    public TransactionTypeService(TransactionTypeRepository repository, TransactionTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public List<TransactionTypeDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public TransactionTypeDTO create(TransactionTypeDTO dto) {
        TransactionType entity = mapper.toEntity(dto);
        TransactionType saved = repository.save(entity);
        return mapper.toDTO(saved);
    }
}
