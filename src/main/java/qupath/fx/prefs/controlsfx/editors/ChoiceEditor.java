/**
 * Copyright 2023 The University of Edinburgh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package qupath.fx.prefs.controlsfx.editors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.PropertySheet;

import java.util.Collection;

/**
 * Editor for choosing from a combo box, which will use an observable list directly if it can
 * (which differs from ControlsFX's default behavior).
 *
 * @param <T>
 */
public class ChoiceEditor<T> extends AbstractChoiceEditor<T, ComboBox<T>> {

    public ChoiceEditor(PropertySheet.Item property, Collection<? extends T> choices) {
        this(property, FXCollections.observableArrayList(choices));
    }

    public ChoiceEditor(PropertySheet.Item property, ObservableList<T> choices) {
        super(new ComboBox<>(), property, choices);
    }

}
