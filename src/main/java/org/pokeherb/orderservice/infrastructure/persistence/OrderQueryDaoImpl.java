package org.pokeherb.orderservice.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.OrderSearchCondition;
import org.pokeherb.orderservice.presentation.dto.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static org.pokeherb.orderservice.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderQueryDaoImpl implements OrderQueryDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderSummaryResponse> search(OrderSearchCondition condition, Pageable pageable) {

        var builder = new BooleanBuilder();

        if (condition.orderUserId() != null)
            builder.and(order.orderUserId.eq(condition.orderUserId()));

        if (condition.productId() != null)
            builder.and(order.productId.eq(condition.productId()));

        if (condition.status() != null)
            builder.and(order.orderStatus.eq(condition.status()));

        var result = queryFactory
                .selectFrom(order)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(OrderSummaryResponse::from)
                .toList();

        var countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(builder);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
