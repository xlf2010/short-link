package com.xlf.repository;

import com.xlf.entity.LinkInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ShortLinkRepository extends JpaRepository<LinkInfoEntity, Long> {
    @Modifying
    @Query(value = "update link_info set status = ?2 where id = ?1", nativeQuery = true)
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
