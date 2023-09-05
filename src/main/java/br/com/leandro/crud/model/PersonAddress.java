package br.com.leandro.crud.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode (callSuper = true)
public class PersonAddress extends AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String address;
	
	@Column
	private String city;
	
	@Column
	private String stateProvince;
	
	@Column
	private String postalCode;
	
	@ManyToOne (fetch = FetchType.EAGER)
	private Country country;	
	
	@ManyToOne (fetch = FetchType.EAGER)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonBackReference
	private Person person;
}
