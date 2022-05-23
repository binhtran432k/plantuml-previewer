package com.github.binhtran432k.plantumlpreviewer.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Loader for PlantUML to isolate plantuml source
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
public class PlantUmlLoader {
    private final Map<Integer, PlantUmlImage> cachedImages = new HashMap<>();
    private SourceStringReader reader;

    public PlantUmlImage getImageFromFile(File file, int index, PlantUmlFormat format) {
        cachedImages.clear();

        try {
            String source = Files.readString(file.toPath());
            reader = new SourceStringReader(source);
        } catch (IOException | NullPointerException e) {
            reader = null;
        }

        return getImageFromBuffer(index, format);
    }

    public PlantUmlImage getImageFromBuffer(int index, PlantUmlFormat format) {
        PlantUmlImage cachedImage = cachedImages.get(index);
        if (cachedImage != null) {
            return cachedImage;
        }

        ByteArrayOutputStream png = new ByteArrayOutputStream();
        try {
            int maxImage = calNumOfImages(reader);
            if (index < 0) {
                index = 0;
            } else if (index + 1 > maxImage) {
                index = maxImage - 1;
            }

            String description = null;
            try {
                description = reader.outputImage(png, index,
                        getFileFormatOption(format)).toString();
            } catch (NoSuchMethodError e) {
                // fallback to deprecated method
                description = reader.generateImage(png, index,
                        getFileFormatOption(format));
            }
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(png.toByteArray()));

            PlantUmlImage plantUMLImage = new PlantUmlImage(index, maxImage, description, image);

            cachedImages.put(index, plantUMLImage);

            return plantUMLImage;
        } catch (IOException | NullPointerException e) {
            cachedImages.clear();

            return getEmptyImage();
        }
    }

    private PlantUmlImage getEmptyImage() {
        return new PlantUmlImage(0, 0, "", null);
    }

    private int calNumOfImages(SourceStringReader reader) {
        return reader.getBlocks().stream().mapToInt(b -> b.getDiagram().getNbImages()).sum();
    }

    private FileFormatOption getFileFormatOption(PlantUmlFormat plantUmlFormat) {
        FileFormat format;
        if (plantUmlFormat == PlantUmlFormat.SVG) {
            format = FileFormat.SVG;
        } else {
            format = FileFormat.PNG;
        }
        return new FileFormatOption(format);
    }
}
