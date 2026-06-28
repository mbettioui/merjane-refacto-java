package com.nimbleways.springboilerplate.infra.persistence.entity;

import lombok.*;

import java.util.Set;

import javax.persistence.*;

// WARN: Should not be changed during the exercise
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToMany
    @JoinTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<ProductEntity> items;
}
