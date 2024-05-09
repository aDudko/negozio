package net.dudko.project.domain.repository;

import net.dudko.project.domain.entity.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderListRepository extends JpaRepository<OrderList, Long> {
}
