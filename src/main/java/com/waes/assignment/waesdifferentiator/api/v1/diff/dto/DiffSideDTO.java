package com.waes.assignment.waesdifferentiator.api.v1.diff.dto;

import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.validation.constraints.AssertTrue;

@Data
public class DiffSideDTO {
    private String diffSideContent;

    @AssertTrue(message = "error.diffSideDTO.doffContent.isNotBase64")
    private boolean isValidContent() {
        return (!this.diffSideContent.isEmpty() && Base64.isBase64(this.diffSideContent));
    }
}
