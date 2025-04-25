package com.clipboard.repository;

import com.clipboard.model.ClipboardItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipboardRepository extends JpaRepository<ClipboardItem, Long> {
    List<ClipboardItem> findByIdLessThan(Long id, Pageable pageable);
    List<ClipboardItem> findByIdGreaterThan(Long id, Sort sort);
}