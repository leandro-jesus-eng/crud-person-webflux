package br.com.leandro.crud.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode (callSuper = true)
public class PersonImage extends AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	//@Basic(fetch = FetchType.EAGER)
	@Column (columnDefinition = "BLOB")
	// @Transient  -- desativa o atributo para o hibernate
	private byte[] image;
	
	@Column
	private String contentType;	
	
	@Column
	private String nameImage;	
	
	@ManyToOne (fetch = FetchType.EAGER)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonBackReference
	private Person person;
}
