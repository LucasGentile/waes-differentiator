package com.waes.assignment.waesdifferentiator.api.v1.diff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="DIFF_PAIR")
public class DiffPair {
    @Id
    @NonNull
    @Column(name="DIFF_ID")
    private Long diffId;

    @Lob
    @Column(name="LEFT_SIDE")
    private String leftSide;

    @Lob
    @Column(name="RIGHT_SIDE")
    private String rightSide;

}
