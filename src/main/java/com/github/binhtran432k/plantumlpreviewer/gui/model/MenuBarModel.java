package com.github.binhtran432k.plantumlpreviewer.gui.model;

import com.github.binhtran432k.plantumlpreviewer.cli.Option;
import com.github.binhtran432k.plantumlpreviewer.gui.view.SubcribeAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for menu bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@Getter
@Setter
public class MenuBarModel extends ModelPublisher {

    private boolean isVisible;

    public MenuBarModel(Option option) {
        this.isVisible = option.isViewMenuBar();
    }

    public void setVisibleAndNotify(boolean isVisible) {
        this.isVisible = isVisible;
        notifySubcribers(SubcribeAction.MENU_BAR);
    }

}
