package com.bar.foo;

import gg.xp.reevent.events.BaseEvent;
import gg.xp.xivsupport.events.actlines.events.AbilityUsedEvent;
import gg.xp.xivsupport.events.actlines.events.HasAbility;
import gg.xp.xivsupport.events.actlines.events.HasEffects;
import gg.xp.xivsupport.events.actlines.events.HasSourceEntity;
import gg.xp.xivsupport.events.actlines.events.HasTargetEntity;
import gg.xp.xivsupport.events.actlines.events.HasTargetIndex;
import gg.xp.xivsupport.events.actlines.events.abilityeffect.AbilityEffect;
import gg.xp.xivsupport.models.XivAbility;
import gg.xp.xivsupport.models.XivCombatant;

import java.util.List;

public class PositionalTriggerResult extends BaseEvent implements HasSourceEntity, HasTargetEntity, HasAbility, HasEffects, HasTargetIndex {

	private final AbilityUsedEvent originalEvent;
	private final boolean positionalHit;

	public PositionalTriggerResult(AbilityUsedEvent originalEvent, boolean positionalHit) {
		this.originalEvent = originalEvent;
		this.positionalHit = positionalHit;
	}

	public boolean isPositionalHit() {
		return positionalHit;
	}

	@Override
	public XivAbility getAbility() {
		return originalEvent.getAbility();
	}

	@Override
	public XivCombatant getSource() {
		return originalEvent.getSource();
	}

	@Override
	public XivCombatant getTarget() {
		return originalEvent.getTarget();
	}

	public List<AbilityEffect> getEffects() {
		return originalEvent.getEffects();
	}

	public long getSequenceId() {
		return originalEvent.getSequenceId();
	}

	public long getTargetIndex() {
		return originalEvent.getTargetIndex();
	}

	public long getNumberOfTargets() {
		return originalEvent.getNumberOfTargets();
	}

	public boolean isFirstTarget() {
		return originalEvent.isFirstTarget();
	}

	public boolean isLastTarget() {
		return originalEvent.isLastTarget();
	}
}
