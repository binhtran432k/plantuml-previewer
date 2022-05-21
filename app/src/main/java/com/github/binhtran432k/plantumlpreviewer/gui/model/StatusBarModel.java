package com.github.binhtran432k.plantumlpreviewer.gui.model;

import com.github.binhtran432k.plantumlpreviewer.gui.view.SubcribeAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for status bar
 *
 * @author Tran Duc Binh
 * @since 1.0.0
 *
 */
@Getter
@Setter
public class StatusBarModel extends ModelPublisher {

    private StatusAction statusAction;

    public void setStatusActionAndNotify(StatusAction statusAction) {
        setStatusAction(statusAction);
        notifySubcribers(SubcribeAction.STATUS_BAR);
    }

}
