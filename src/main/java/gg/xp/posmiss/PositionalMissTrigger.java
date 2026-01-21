package gg.xp.posmiss;

import gg.xp.reevent.events.EventContext;
import gg.xp.reevent.scan.HandleEvents;
import gg.xp.xivdata.data.duties.KnownDuty;
import gg.xp.xivsupport.callouts.CalloutRepo;
import gg.xp.xivsupport.callouts.ModifiableCallout;
import gg.xp.xivsupport.callouts.OverridesCalloutGroupEnabledSetting;
import gg.xp.xivsupport.events.actlines.events.AbilityUsedEvent;
import gg.xp.xivsupport.events.actlines.events.abilityeffect.DamageTakenEffect;
import gg.xp.xivsupport.persistence.settings.BooleanSetting;

import java.util.List;

@CalloutRepo(name = "Positional Miss Callouts", duty = KnownDuty.None)
public class PositionalMissTrigger implements OverridesCalloutGroupEnabledSetting {

	private final BooleanSetting enabled;
	private final ModifiableCallout<PositionalTriggerResult> hit = new ModifiableCallout<PositionalTriggerResult>("Positional Hit", "Hit", 2000).autoIcon().disabledByDefault();
	private final ModifiableCallout<PositionalTriggerResult> miss = new ModifiableCallout<PositionalTriggerResult>("Positional Missed", "{event.ability} missed", 2000).autoIcon();
	private final PositionalMissBackend backend;

	public PositionalMissTrigger(PositionalMissBackend backend) {
		enabled = backend.getEnabled();
		this.backend = backend;
	}

	@HandleEvents
	public void abilityUsed(EventContext context, AbilityUsedEvent event) {
		if (!event.getSource().isThePlayer()) {
			return;
		}
		// Takes care of ability ID + level
		List<PositionalInfo> candidates = backend.forAbility(event);
		// TODO: does this also apply to blocked/parried or does that not matter?
		for (PositionalInfo positionalInfo : candidates) {
			List<DamageTakenEffect> effects = event.getEffectsOfType(DamageTakenEffect.class);
			for (DamageTakenEffect effect : effects) {
				int bonusPercent = effect.getComboBonus();
				if (bonusPercent == positionalInfo.bonusHit() || bonusPercent == positionalInfo.comboBonusHit() ||
						(positionalInfo.fixedHitPercent() != null && bonusPercent == positionalInfo.fixedHitPercent())) {
					context.accept(new PositionalTriggerResult(event, true));
					return;
				}
				else if (bonusPercent == positionalInfo.bonusMiss() || bonusPercent == positionalInfo.comboBonusMiss() ||
						(positionalInfo.fixedMissPercent() != null && bonusPercent == positionalInfo.fixedMissPercent())) {
					context.accept(new PositionalTriggerResult(event, false));
					return;
				}
			}
		}
	}

	@HandleEvents
	public void handlePositional(EventContext context, PositionalTriggerResult event) {
		if (event.isPositionalHit()) {
			context.accept(hit.getModified(event));
		}
		else {
			context.accept(miss.getModified(event));
		}
	}

	@Override
	public BooleanSetting getCalloutGroupEnabledSetting() {
		return enabled;
	}
}
