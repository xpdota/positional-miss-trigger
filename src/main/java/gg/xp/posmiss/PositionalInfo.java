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
	private final @Nullable Integer potencyComboMiss;
	private final @Nullable Integer potencyComboHit;
	private final @Nullable Integer minLevel;
	private final @Nullable Integer maxLevel;

	@JsonCreator
	public PositionalInfo(
			@JsonProperty("key") String key,
			@JsonProperty("abilityId") @Nullable Long abilityId,
			@JsonProperty("abilityIdHex") @Nullable String abilityIdHex,
			@JsonProperty("potencyMiss") int potencyMiss,
			@JsonProperty("potencyHit") int potencyHit,
			@JsonProperty("potencyComboMiss") @Nullable Integer potencyComboMiss,
			@JsonProperty("potencyComboHit") @Nullable Integer potenctyComboHit,
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
		this.potencyComboMiss = potencyComboMiss;
		this.potencyComboHit = potenctyComboHit;
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

	public @Nullable Integer potencyComboMiss() {
		return potencyComboMiss;
	}

	public @Nullable Integer potencyComboHit() {
		return potencyComboHit;
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
				Objects.equals(this.potencyComboMiss, that.potencyComboMiss) &&
				Objects.equals(this.potencyComboHit, that.potencyComboHit) &&
				Objects.equals(this.minLevel, that.minLevel) &&
				Objects.equals(this.maxLevel, that.maxLevel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, abilityId, potencyMiss, potencyHit, potencyComboMiss, potencyComboHit, minLevel, maxLevel);
	}

	@Override
	public String toString() {
		return "PositionalInfo[" +
				"key=" + key + ", " +
				"abilityId=" + abilityId + ", " +
				"potencyMiss=" + potencyMiss + ", " +
				"potencyHit=" + potencyHit + ", " +
				"comboPotencyMiss=" + potencyComboMiss + ", " +
				"comboPotencyHit=" + potencyComboHit + ", " +
				"minLevel=" + minLevel + ", " +
				"maxLevel=" + maxLevel + ']';
	}


	public int bonusHit() {
		return Math.floorDiv(100 * (potencyHit - potencyMiss), potencyHit);
	}

	public int comboBonusHit() {
		if (potencyComboHit == null) {
			// -1 will never match
			return -1;
		}
		return Math.floorDiv(100 * (potencyComboHit - potencyMiss), potencyComboHit);
	}

	@SuppressWarnings("MethodMayBeStatic")
	public int bonusMiss() {
		return 0;
	}

	public int comboBonusMiss() {
		if (potencyComboMiss == null) {
			// -1 will never match
			return -1;
		}
		return Math.floorDiv(100 * (potencyComboMiss - potencyMiss), potencyComboMiss);
	}

	public String getComment() {
		return comment;
	}
}
