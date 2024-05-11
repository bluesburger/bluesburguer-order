package br.com.bluesburguer.order.adapters.out.persistence.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.bluesburguer.order.core.domain.OrderFase;
import br.com.bluesburguer.order.core.domain.OrderStep;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "bluesburguer-order", name = "TB_ORDER")
@Builder
public class Order implements Serializable {
	
	private static final long serialVersionUID = 4781858089323528412L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @Default
    @NotNull
    private Double totalValue = 0D;
    
    @Setter
    @Default
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStep step = OrderStep.ORDER;
    
    @Setter
    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderFase fase;

    @Default
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();
    
    @NonNull
    @NotNull
	@ManyToOne
    @JoinColumn(name = "user_id")
	private OrderUser user;
    
    public void add(OrderItem... items) {
    	this.items.addAll(List.of(items));
    }
    
    public void remove(OrderItem... itens) {
    	this.items.removeAll(List.of(itens));
    }
}
