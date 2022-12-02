package gg.xp.posmiss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class PositionalInfo {
	private final String key;
	private final String comment;
	private final long abilityId;
	private final int potencyMiss;
	private final int potencyHit;
	private final @Nullable Integer comboPotencyMiss;
	private final @Nullable Integer comboPotencyHit;
	private final @Nullable Integer minLevel;
	private final @Nullable Integer maxLevel;

	@JsonCreator
	public PositionalInfo(
			@JsonProperty("key") String key,
			@JsonProperty("abilityId") @Nullable Long abilityId,
			@JsonProperty("abilityIdHex") @Nullable String abilityIdHex,
			@JsonProperty("potencyMiss") int potencyMiss,
			@JsonProperty("potencyHit") int potencyHit,
			@JsonProperty("comboPotencyMiss") @Nullable Integer comboPotencyMiss,
			@JsonProperty("comboPotencyHit") @Nullable Integer comboPotencyHit,
			@JsonProperty("minLevel") @Nullable Integer minLevel,
			@JsonProperty("maxlevel") @Nullable Integer maxLevel,
			@JsonProperty("comment") @Nullable String comment
	) {
		this.key = key;
		this.comment = comment;
		if (abilityId == null) {
			if (abilityIdHex == null) {
				throw new IllegalArgumentException("Either abilityId or abilityIdHex must be specified");
			}
			this.abilityId = Long.parseLong(abilityIdHex, 16);
		}
		else {
			this.abilityId = abilityId;
		}
		this.potencyMiss = potencyMiss;
		this.potencyHit = potencyHit;
		this.comboPotencyMiss = comboPotencyMiss;
		this.comboPotencyHit = comboPotencyHit;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	public boolean validForLevel(long level) {
		return (minLevel == null || level >= minLevel)
				&& (maxLevel == null || level <= maxLevel);
	}

	public String key() {
		return key;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public Integer getMinLevel() {
		return minLevel;
	}

	public long abilityId() {
		return abilityId;
	}

	public int potencyMiss() {
		return potencyMiss;
	}

	public int potencyHit() {
		return potencyHit;
	}

	public @Nullable Integer comboPotencyMiss() {
		return comboPotencyMiss;
	}

	public @Nullable Integer comboPotencyHit() {
		return comboPotencyHit;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (PositionalInfo) obj;
		return Objects.equals(this.key, that.key) &&
				this.abilityId == that.abilityId &&
				this.potencyMiss == that.potencyMiss &&
				this.potencyHit == that.potencyHit &&
				Objects.equals(this.comboPotencyMiss, that.comboPotencyMiss) &&
				Objects.equals(this.comboPotencyHit, that.comboPotencyHit) &&
				Objects.equals(this.minLevel, that.minLevel) &&
				Objects.equals(this.maxLevel, that.maxLevel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, abilityId, potencyMiss, potencyHit, comboPotencyMiss, comboPotencyHit, minLevel, maxLevel);
	}

	@Override
	public String toString() {
		return "PositionalInfo[" +
				"key=" + key + ", " +
				"abilityId=" + abilityId + ", " +
				"potencyMiss=" + potencyMiss + ", " +
				"potencyHit=" + potencyHit + ", " +
				"comboPotencyMiss=" + comboPotencyMiss + ", " +
				"comboPotencyHit=" + comboPotencyHit + ", " +
				"minLevel=" + minLevel + ", " +
				"maxLevel=" + maxLevel + ']';
	}


	public int bonusHit() {
		return Math.floorDiv(potencyHit - potencyMiss, potencyHit);
	}

	public int comboBonusHit() {
		if (comboPotencyHit == null) {
			// -1 will never match
			return -1;
		}
		return Math.floorDiv(comboPotencyHit - potencyMiss, comboPotencyHit);
	}

	@SuppressWarnings("MethodMayBeStatic")
	public int bonusMiss() {
		return 0;
	}

	public int comboBonusMiss() {
		if (comboPotencyMiss == null) {
			// -1 will never match
			return -1;
		}
		return Math.floorDiv(comboPotencyMiss - potencyMiss, comboPotencyMiss);
	}

	public String getComment() {
		return comment;
	}
}
