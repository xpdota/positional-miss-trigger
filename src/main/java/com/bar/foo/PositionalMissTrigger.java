package com.bar.foo;

import gg.xp.reevent.events.EventContext;
import gg.xp.reevent.scan.HandleEvents;
import gg.xp.xivdata.data.duties.KnownDuty;
import gg.xp.xivsupport.callouts.CalloutRepo;
import gg.xp.xivsupport.callouts.ModifiableCallout;
import gg.xp.xivsupport.events.actlines.events.AbilityUsedEvent;

@CalloutRepo(name = "Positional Miss Callouts", duty = KnownDuty.None)
public class PositionalMissTrigger {

	private final ModifiableCallout<PositionalTriggerResult> hit = new ModifiableCallout<PositionalTriggerResult>("Positional Hit", "Hit", 1000).autoIcon().disabledByDefault();
	private final ModifiableCallout<PositionalTriggerResult> miss = new ModifiableCallout<PositionalTriggerResult>("Positional Missed", "{event.ability} missed", 1000).autoIcon();


	@HandleEvents
	public void abilityUsed(EventContext context, AbilityUsedEvent event) {
		// TODO
	}

	@HandleEvents
	public void handlePositional(EventContext context, PositionalTriggerResult event) {
		if (event.isPositionalHit()) {
			hit.getModified(event);
		}
		else {
			miss.getModified(event);
		}
	}

}
