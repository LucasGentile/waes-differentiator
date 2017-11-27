package com.waes.assignment.waesdifferentiator.api.v1.diff.dto;

import com.waes.assignment.waesdifferentiator.api.v1.diff.enums.DiffResultType;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;
import lombok.Data;

@Data
public class DiffResultDTO {
    private DiffPair diffPair;
    private String diffOffsets;
    private DiffResultType diffResultType;
}
