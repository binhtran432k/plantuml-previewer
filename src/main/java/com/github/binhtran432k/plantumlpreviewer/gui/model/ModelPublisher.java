package com.github.binhtran432k.plantumlpreviewer.gui.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.binhtran432k.plantumlpreviewer.gui.view.IViewSubcriber;
import com.github.binhtran432k.plantumlpreviewer.gui.view.SubcribeAction;

/**
 * Publisher for model in Observer design pattern
 *
 * @author Tran Duc Binh
 *
 */
public abstract class ModelPublisher {

    private final Map<SubcribeAction, Set<IViewSubcriber>> subcribers = new HashMap<>();

    public final void subcribe(SubcribeAction action, IViewSubcriber subcriber) {
        Set<IViewSubcriber> actionSubcribers = subcribers.get(action);
        if (actionSubcribers == null) {
            actionSubcribers = new HashSet<>();
            subcribers.put(action, actionSubcribers);
        }
        actionSubcribers.add(subcriber);
    }

    public final void unsubcribe(SubcribeAction action, IViewSubcriber subcriber) {
        Set<IViewSubcriber> actionSubcribers = subcribers.get(action);
        if (actionSubcribers != null) {
            actionSubcribers.remove(subcriber);
        }
    }

    public final void notifySubcribers(SubcribeAction action) {
        Set<IViewSubcriber> actionSubcribers = subcribers.get(action);
        if (actionSubcribers != null) {
            actionSubcribers.forEach(subcriber -> subcriber.update(action));
        }
    }

    public final void notifyAllSubcribers() {
        subcribers.forEach((action, actionSubcribers) -> {
            actionSubcribers.forEach(subcriber -> subcriber.update(action));
        });
    }

}
