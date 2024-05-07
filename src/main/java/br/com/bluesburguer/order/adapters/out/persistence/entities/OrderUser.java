package br.com.bluesburguer.order.adapters.out.persistence.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.CreationTimestamp;

import br.com.bluesburguer.order.core.domain.Cpf;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "bluesburguer-order", name = "TB_USER")
@Builder
public class OrderUser implements Serializable {

	private static final long serialVersionUID = -3541042247074947720L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Setter
	private String cpf;
	
	@Setter
	private String email;
	
	@CreationTimestamp
    private LocalDateTime creationDateTime;
	
	@Default
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
	
	public OrderUser(Long id, Cpf cpf, String email) {
		if (ObjectUtils.isEmpty(cpf) && ObjectUtils.isEmpty(email)) {
			throw new IllegalArgumentException("Usuário precisa estar identificado por cpf ou email");
		}
		
		this.cpf = cpf.getValue();
		this.email = email;
	}
	
	public OrderUser(Cpf cpf, String email) {
		this(null, cpf, email);
	}
}
