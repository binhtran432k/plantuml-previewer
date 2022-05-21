package com.github.binhtran432k.plantumlpreviewer.core;

import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PlauntUML image use for return from loader
 *
 * @author Tran Duc Binh
 *
 */
@AllArgsConstructor
@Getter
public class PlantUmlImage {

    private int index;
    private int maxImage;
    private String description;
    private BufferedImage image;

}
