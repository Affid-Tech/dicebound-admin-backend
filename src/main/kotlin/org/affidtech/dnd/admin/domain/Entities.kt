package org.affidtech.dnd.admin.domain

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*


/**
 * Базовая сущность с UUID‑ключом. Наследуется всеми JPA‑классами проекта.
 */
@MappedSuperclass
interface BaseEntity {
	/**
	 * Первичный ключ. Генерируется на стороне приложения, чтобы REST‑клиент
	 * мог ссылаться на объект до его сохранения (idempotent‑PUT / PATCH).
	 */
	val id: UUID?
}

/* --------------------------------------------------------------------- */
/*                       CORE USER + ROLE PROFILES                       */
/* --------------------------------------------------------------------- */

@Entity
@Table(name = "user_profile")
data class UserEntity(
	@Id
	override val id: UUID? = null,
	
	@Column(nullable = false, length = 120)
	var name: String,
	
	@Column(length = 255)
	var email: String? = null,
	
	@Column(columnDefinition = "text")
	var bio: String? = null,
) : BaseEntity

@Entity
@Table(name = "player_profile")
data class PlayerProfileEntity(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: UserEntity,
	
	@Id
	override val id: UUID? = null
) : BaseEntity

@Entity
@Table(name = "dungeon_master_profile")
data class DungeonMasterProfileEntity(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: UserEntity,
	
	@Id
	override val id: UUID? = null
) : BaseEntity

@Entity
@Table(name = "admin_profile")
data class AdminProfileEntity(
	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: UserEntity,
	
	@Id
	override val id: UUID? = null
) : BaseEntity

/* --------------------------------------------------------------------- */
/*                          ADVENTURE & SESSION                          */
/* --------------------------------------------------------------------- */

/**
 * Enum отражает тип приключения (ваншот, мультишот, кампания).
 */
enum class AdventureType { ONESHOT, MULTISHOT, CAMPAIGN }

enum class AdventureStatus { PLANNED, RECRUITING, IN_PROGRESS, COMPLETED, CANCELLED }

@Entity
@Table(name = "adventure")
data class AdventureEntity(
	@Id
	override val id: UUID? = null,
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	var type: AdventureType,
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	var status: AdventureStatus = AdventureStatus.PLANNED,
	
	@Column(name = "game_system", nullable = false, columnDefinition = "text")
	var gameSystem: String,
	
	@Column(length = 160, nullable = false)
	var title: String,
	
	@Column(nullable = true)
	var coverUrl: String?,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dm_id", nullable = false)
	var dungeonMaster: UserEntity,
	
	@Column(columnDefinition = "text")
	var description: String? = null,
	
	@Column(name = "start_level")
	var startLevel: Short? = null,
	
	@Column(name = "min_players", nullable = false)
	var minPlayers: Short,
	
	@Column(name = "max_players", nullable = false)
	var maxPlayers: Short,
	
	@Column(name = "price_units")
	var priceUnits: Int? = null,
	
	@OneToMany(mappedBy = "adventure", cascade = [CascadeType.ALL], orphanRemoval = true)
	var sessions: MutableList<GameSessionEntity> = mutableListOf(),
	
	@OneToMany(mappedBy = "adventure", cascade = [CascadeType.ALL], orphanRemoval = true)
	var signups: MutableList<AdventureSignupEntity> = mutableListOf(),
) : BaseEntity

@Entity
@Table(name = "game_session")
data class GameSessionEntity(
	@Id
	override val id: UUID? = null,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "adventure_id", nullable = false)
	var adventure: AdventureEntity,
	
	@Column(name = "start_time", nullable = false)
	var startTime: OffsetDateTime,
	
	@Column(name = "duration_hours", nullable = false)
	var durationHours: Short,
	
	@Column(name = "link_foundry")
	var linkFoundry: String? = null,
	
	@Column(columnDefinition = "text")
	var notes: String? = null,
) : BaseEntity


/**
 * Enum отражает тип приключения (ваншот, мультишот, кампания).
 */
enum class AdventureSignupStatus { PENDING, APPROVED, CANCELED }

@Entity
@Table(
	name = "adventure_signup"
)
data class AdventureSignupEntity(
	@Id
	override val id: UUID? = UUID.randomUUID(),
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "adventure_id", nullable = false)
	var adventure: AdventureEntity,
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	var user: UserEntity,
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var status: AdventureSignupStatus? = AdventureSignupStatus.PENDING,
) : BaseEntity

/* --------------------------------------------------------------------- */
/*                             CURRENCY RATE                             */
/* --------------------------------------------------------------------- */

@Entity
@Table(name = "currency_rate")
data class CurrencyRateEntity(
	@Id
	@Column(length = 3)
	val currency: String,
	
	@Column(name = "ratio", nullable = false)
	var ratio: Int,
	
	@Column(name = "updated_at", nullable = false)
	var updatedAt: OffsetDateTime? = null,
) {
	
	@PrePersist
	fun onCreate() {
		if (updatedAt == null) updatedAt = OffsetDateTime.now()
	}
	
	@PreUpdate
	fun onUpdate() {
		updatedAt = OffsetDateTime.now()
	}
}
