package br.com.leandro.crud.repository;

import br.com.leandro.crud.model.Person;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@NoRepositoryBean
public interface PersonRepository<T, ID>  extends ReactiveCrudRepository<Person, Long> {
}
