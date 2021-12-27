package com.vodafone.parkingticket.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Slot {
    private Integer occupiedInitialSlotIndex;
    private Integer occupiedLastSlotIndex;
    private Integer occupiedIndexLength;
    private Vehicle vehicle;
}
