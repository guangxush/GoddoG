package com.shgx.subbustwo.reporsitory;

import com.shgx.subbustwo.model.BusinessTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Repository
public interface BusinessTwoRepo extends JpaRepository<BusinessTwo, Long> {

    Optional<BusinessTwo> findByUid(String uid);

    Optional<List<BusinessTwo>> findAllByUid(String uid);
}
