package br.com.bluesburguer.order.infra.database.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "bluesburguer-order", name = "TB_USER")
@EqualsAndHashCode(exclude = { "orders" })
@Builder
public class OrderUserEntity implements Serializable {

	private static final long serialVersionUID = -3541042247074947720L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
	
	@Setter
	@Column(name = "CPF")
	private String cpf;
	
	@Setter
	@Column(name = "EMAIL")
	private String email;
	
	@CreationTimestamp
	@Column(name = "CREATION_DATE_TIME")
    private LocalDateTime creationDateTime;
	
	@Default
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderEntity> orders = new ArrayList<>();
}
