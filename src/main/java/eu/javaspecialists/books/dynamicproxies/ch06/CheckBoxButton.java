/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.javaspecialists.books.dynamicproxies.ch06;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.*;
import static javax.swing.WindowConstants.*;

public class CheckBoxButton {
    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("CheckBoxButton");
            frame.setLayout(new FlowLayout());

            // tag::listing[]
            var button = new JButton();
            button.setLayout(new GridLayout(0, 1));
            button.add(new JLabel("Delete All Files", CENTER));
            button.add(new JCheckBox("Are you sure?"));
            // end::listing[]

            frame.add(button);
            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
