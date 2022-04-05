package com.school.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Options {

    private String optionsName;

    private String optionsCategory;

    private double optionsPrice;

    private double optionsCostToAdd;
}
