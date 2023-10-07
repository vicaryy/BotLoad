package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.ActiveRequestEntity;
import org.vicary.repository.ActiveRequestRepository;

@Service
@RequiredArgsConstructor
public class ActiveRequestService {
    private final ActiveRequestRepository repository;

    public ActiveRequestEntity saveActiveUser(ActiveRequestEntity activeRequestEntity) {
        return repository.save(activeRequestEntity);
    }

    public boolean existsByUserId(String userId) {
        return repository.existsByUserId(userId);
    }

    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAllActiveUsers() {
        repository.deleteAll();
    }

    public int countActiveUsers() {
        return (int) repository.count();
    }
}
