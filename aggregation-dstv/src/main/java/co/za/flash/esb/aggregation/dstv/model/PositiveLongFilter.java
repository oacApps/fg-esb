package co.za.flash.esb.aggregation.dstv.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PositiveLongFilter {
    @Override
    public boolean equals(Object other) {
        // Trick required to be compliant with the Jackson Custom attribute processing
        if (other == null) {
            return true;
        }
        Long value = (Long) other;
        return value <= 0;
    }
}
