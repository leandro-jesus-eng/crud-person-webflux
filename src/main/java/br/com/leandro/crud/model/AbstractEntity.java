package br.com.leandro.crud.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractEntity {
	
	public abstract Long getId();
	
	@Override
	@Hidden
	public int hashCode() {
		if (getId() != null && getId() != 0) return (int) ( getId() % Integer.MAX_VALUE -1);		
		return super.hashCode();
	}
	
	@Hidden
	public int getHashCode() {
		return hashCode();
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (hashCode() == o.hashCode()) return true;                
        
        return super.equals(o);
    }
}
