package gg.xp.posmiss;

import gg.xp.reevent.scan.ScanMe;
import gg.xp.xivsupport.gui.TitleBorderFullsizePanel;
import gg.xp.xivsupport.gui.extra.PluginTab;

import javax.swing.*;
import java.awt.*;

@ScanMe
public class PositionalMissGui implements PluginTab {

	@Override
	public String getTabName() {
		return "Positionals";
	}

	@Override
	public Component getTabContents() {
		TitleBorderFullsizePanel panel = new TitleBorderFullsizePanel("Positional Miss Trigger");
		panel.add(new JLabel("TODO: write the code for this"));
		return panel;
	}
}
