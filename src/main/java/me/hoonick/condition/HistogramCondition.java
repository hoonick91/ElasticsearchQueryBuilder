package me.hoonick.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString
@Builder
public class HistogramCondition {
    String field;
    String interval;
    String format;
    String time_zone;
    String min_doc_count;
}
