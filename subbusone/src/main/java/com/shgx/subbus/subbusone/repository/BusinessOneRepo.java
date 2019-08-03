package com.shgx.subbus.subbusone.repository;

import com.shgx.subbus.subbusone.model.BusinessOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Repository
public interface BusinessOneRepo extends JpaRepository<BusinessOne, Long> {

    Optional<BusinessOne> findByUid(String uid);

    Optional<List<BusinessOne>> findAllByUid(String uid);
}
