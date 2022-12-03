package gg.xp.posmiss;

import gg.xp.reevent.scan.ScanMe;
import gg.xp.xivdata.data.ActionLibrary;
import gg.xp.xivsupport.gui.TitleBorderFullsizePanel;
import gg.xp.xivsupport.gui.extra.PluginTab;
import gg.xp.xivsupport.gui.tables.CustomColumn;
import gg.xp.xivsupport.gui.tables.CustomTableModel;
import gg.xp.xivsupport.gui.tables.renderers.ActionAndStatusRenderer;
import gg.xp.xivsupport.models.XivAbility;
import gg.xp.xivsupport.persistence.gui.BooleanSettingGui;

import javax.swing.*;
import java.awt.*;

@ScanMe
public class PositionalMissGui implements PluginTab {

	private final PositionalMissBackend backend;

	public PositionalMissGui(PositionalMissBackend backend) {
		this.backend = backend;
	}

	@Override
	public String getTabName() {
		return "Positionals";
	}

	@Override
	public Component getTabContents() {
		TitleBorderFullsizePanel panel = new TitleBorderFullsizePanel("Positional Miss Trigger");
		panel.setLayout(new BorderLayout());
		CustomTableModel<PositionalInfo> model = CustomTableModel.builder(backend::getPositionals)
				.addColumn(new CustomColumn<>("Ability", pi -> {
					String name = ActionLibrary.forId(pi.abilityId()).name();
					if (pi.getComment() != null) {
						name += " (" + pi.getComment() + ')';
					}
					return new XivAbility(pi.abilityId(), name);
				}, c -> c.setCellRenderer(new ActionAndStatusRenderer())))
				.addColumn(new CustomColumn<>("Base Potency",
						PositionalInfo::potencyMiss))
				.addColumn(new CustomColumn<>("Positional Potency",
						positionalInfo -> String.format("%s (%s%%)", positionalInfo.potencyHit(), positionalInfo.bonusHit())))
				.addColumn(new CustomColumn<>("Combo Potency",
						positionalInfo -> {
							Integer pot = positionalInfo.potencyComboMiss();
							if (pot == null) {
								return null;
							}
							return String.format("%s (%s%%)", pot, positionalInfo.comboBonusMiss());
						}))
				.addColumn(new CustomColumn<>("Pos+Combo Potency",
						positionalInfo -> {
							Integer pot = positionalInfo.potencyComboHit();
							if (pot == null) {
								return null;
							}
							return String.format("%s (%s%%)", pot, positionalInfo.comboBonusHit());
						}))
				.addColumn(new CustomColumn<>("Level Range", pi -> {
					Integer min = pi.getMinLevel();
					Integer max = pi.getMaxLevel();
					if (min == null && max == null) {
						return "";
					}
					if (min == null) {
						return "1 - %d".formatted(max);
					}
					else {
						if (max == null) {
							return "%d+".formatted(min);
						}
						else {
							return "%d - %d".formatted(min, max);
						}
					}
				})).build();
		JTable table = model.makeTable();
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		{
			JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			topPanel.add(new BooleanSettingGui(backend.getEnabled(), "Enabled", true).getComponent());
			panel.add(topPanel, BorderLayout.NORTH);
		}
		return panel;
	}
}
