package com.waes.assignment.waesdifferentiator.api.v1.validation.dto;

import com.waes.assignment.waesdifferentiator.api.v1.validation.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private MessageType type;
    private String message;
}
