package br.com.leandro.crud.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
//@EqualsAndHashCode (callSuper = true)
@Builder
public class Person { // extends AbstractEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String firstName;
	
	@Column  (columnDefinition = "VARCHAR(512)")
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column
	private Date birthday;

	@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn (name = "person_id")
	@ToString.Exclude
	@JsonManagedReference
	private List<PersonImage> personImages = new ArrayList<>();

	@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn (name = "person_id")
	@ToString.Exclude // Não gera toString() para entrar em loop com a outra clasee q também tem referência para cá.
	@JsonManagedReference // para evitar loop no momento q vai serializar o objeto
	private Set<PersonAddress> personAddresses = new HashSet<>();

}
