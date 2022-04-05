package com.school.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Tariff {

    private String tariffName;

    private double tariffPrice;

    private List<Options> connectedOptions;
}
