package br.com.bluesburguer.orderingsystem.order.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "bluesburguer-order", name = "TB_ORDER_ITEM")
@Builder
public class OrderItem implements Serializable {
	
	private static final long serialVersionUID = 4781858089323528412L;

	@Id
    private Long id;
	
	@Setter
	@ManyToOne
    @JoinColumn(name = "order_id")
	private Order order;
	
	@Default
	@Column
	private Double currentValue = 0D;
	
	@CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
