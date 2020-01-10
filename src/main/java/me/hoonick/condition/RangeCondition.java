package me.hoonick.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Builder
public class RangeCondition {
    Object gte;
    Object lte;
    String format;
}
