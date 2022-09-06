package spring.core.discount;

import spring.core.member.Member;

public interface DiscountPolicy {

    int discount(Member member, int price);
}
