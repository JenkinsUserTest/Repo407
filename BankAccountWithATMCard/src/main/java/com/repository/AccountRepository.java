package com.repository;
import java.util.List;
import com.bean.Account;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
@Repository
public interface AccountRepository extends CrudRepository<Account,Integer>  {

}
