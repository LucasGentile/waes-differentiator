package com.waes.assignment.waesdifferentiator.api.v1.diff.model;

import lombok.Data;

@Data
public class DiffResultDTO {
    private DiffPair diffPair;
    private String result;
}
