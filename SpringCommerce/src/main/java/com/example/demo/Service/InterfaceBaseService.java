package com.example.demo.Service;

import com.example.demo.Model.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.UUID;

public interface InterfaceBaseService <M extends BaseModel, R extends JpaRepository<M, UUID>> {
    public M insert(M model);
    public M getById(UUID id);
    public M update(UUID id, M model);
    public void deleteById(UUID id);
    public List<M> getAll();
    public Page<M> getPage(int pageIndex, int pageSize);
}
