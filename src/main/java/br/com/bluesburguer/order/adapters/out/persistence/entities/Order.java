package br.com.bluesburguer.order.adapters.out.persistence.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@ToString(exclude = "items")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "bluesburguer-order", name = "TB_ORDER")
@Builder
public class Order implements Serializable {
	
	private static final long serialVersionUID = 4781858089323528412L;

	@Id
	@Setter
    @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "ID", columnDefinition = "varchar(100)")
    private String id;

    @CreationTimestamp
    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "UPDATED_TIME")
    private LocalDateTime updatedTime;
    
    @Setter
    @Default
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "STEP")
    private OrderStep step = OrderStep.ORDER;
    
    @Setter
    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "FASE")
    private OrderFase fase;

    @Default
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @NonNull
    @NotNull
	@ManyToOne
    @JoinColumn(name = "USER_ID")
	private OrderUser user;
    
    public void add(OrderItem... items) {
    	this.items.addAll(List.of(items));
    }
    
    public void remove(OrderItem... itens) {
    	this.items.removeAll(List.of(itens));
    }
}
