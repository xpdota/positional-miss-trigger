package gg.xp.posmiss;

import gg.xp.reevent.scan.ScanMe;
import gg.xp.xivsupport.events.actlines.events.AbilityUsedEvent;
import gg.xp.xivsupport.persistence.PersistenceProvider;
import gg.xp.xivsupport.persistence.settings.BooleanSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ScanMe
public class PositionalMissBackend {

	private static final Logger log = LoggerFactory.getLogger(PositionalMissBackend.class);
	private static final ExecutorService exs = Executors.newSingleThreadExecutor();

	private final List<PositionalInfo> positionals;
	private final Map<Long, List<PositionalInfo>> perAbility = new HashMap<>();
	private final BooleanSetting enabled;

	public PositionalMissBackend(PersistenceProvider pers) {
		enabled = new BooleanSetting(pers, "positional-miss.enabled", true);
		ObjectMapper mapper = new ObjectMapper();
		positionals = Collections.unmodifiableList(mapper.readValue(PositionalMissBackend.class.getResourceAsStream("/positionals.json"), new TypeReference<>() {
		}));
		positionals.forEach(pos -> {
			perAbility.computeIfAbsent(pos.abilityId(), unused -> new ArrayList<>()).add(pos);
		});
	}

	public List<PositionalInfo> getPositionals() {
		return positionals;
	}

	public List<PositionalInfo> forAbility(AbilityUsedEvent event) {
		List<PositionalInfo> candidates = perAbility.get(event.getAbility().getId());
		if (candidates == null) {
			return Collections.emptyList();
		}
		long level = event.getSource().getLevel();
		return candidates.stream().filter(pi -> pi.validForLevel(level)).toList();
	}

	public BooleanSetting getEnabled() {
		return enabled;
	}
}
