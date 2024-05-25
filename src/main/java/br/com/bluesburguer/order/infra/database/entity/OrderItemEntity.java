package br.com.bluesburguer.order.infra.database.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "bluesburguer-order", name = "TB_ORDER_ITEM")
@Builder
public class OrderItemEntity implements Serializable {
	
	private static final long serialVersionUID = 4781858089323528412L;

	@Id
	@Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
	
	@NonNull
	@NotNull
	@Column(name = "ORDER_ITEM_ID")
	private Long orderItemId;
	
	@Setter
	@NonNull
	@NotNull
	@ManyToOne
    @JoinColumn(name = "ORDER_ID")
	private OrderEntity order;
	
	@Setter
	@Default
	@Column(name = "QUANTITY")
	private Integer quantity = 1;
	
	@CreationTimestamp
	@Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "UPDATED_TIME")
    private LocalDateTime updatedTime;
}
