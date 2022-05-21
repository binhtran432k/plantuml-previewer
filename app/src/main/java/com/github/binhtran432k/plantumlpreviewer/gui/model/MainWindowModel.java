package com.github.binhtran432k.plantumlpreviewer.gui.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for Main window
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@Getter
@Setter
public class MainWindowModel extends ModelPublisher {

    private String title = "PlantUML Previewer";
    private PrincipalPanelType principalpanelType = PrincipalPanelType.PREVIEW;

}
