package com.shgx.business.business.repository;

import com.shgx.business.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: guangxush
 * @create: 2019/08/02
 */
@Repository
public interface BusinessRepo extends JpaRepository<Business, Long> {

    Optional<Business> findByUid(String uid);

    Optional<List<Business>> findAllByUid(String uid);
}
