package org.pokeherb.orderservice.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.domain.repository.OrderQueryRepository;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static org.pokeherb.orderservice.domain.entity.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderQueryDaoImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderSummaryResponse> search(OrderSearchConditionRequestDto condition, Pageable pageable) {

        var builder = new BooleanBuilder();

        if (condition.orderUserId() != null)
            builder.and(order.orderUserId.eq(condition.orderUserId()));

        if (condition.productId() != null)
            builder.and(order.productId.eq(condition.productId()));

        if (condition.status() != null)
            builder.and(order.orderStatus.eq(condition.status()));

        if(condition.productName() != null && !condition.productName().isBlank())
            builder.and(order.productName.containsIgnoreCase(condition.productName()));
        if(condition.requestVendorId() != null)
            builder.and(order.receiveVendorId.eq(condition.requestVendorId()));

        // 소프트 딜리트 된거 제외
        builder.and(order.deletedAt.isNull());

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
