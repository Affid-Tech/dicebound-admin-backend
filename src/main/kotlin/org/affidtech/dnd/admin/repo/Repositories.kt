package org.affidtech.dnd.admin.repo

import org.affidtech.dnd.admin.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.OffsetDateTime
import java.util.*

// --------------------------- Core -------------------------------------------
interface UserRepository : JpaRepository<UserEntity, UUID>

interface PlayerProfileRepository : JpaRepository<PlayerProfileEntity, UUID> {
	fun existsByUser(user: UserEntity): Boolean
	fun deleteByUserId(userId: UUID)
}

interface DungeonMasterProfileRepository : JpaRepository<DungeonMasterProfileEntity, UUID> {
	fun existsByUser(user: UserEntity): Boolean
	fun deleteByUserId(userId: UUID)
}

interface AdminProfileRepository : JpaRepository<AdminProfileEntity, UUID> {
	fun existsByUser(user: UserEntity): Boolean
	fun deleteByUserId(userId: UUID)
}


// --------------------------- Adventure --------------------------------------
interface AdventureRepository : JpaRepository<AdventureEntity, UUID> {
	fun findByType(type: AdventureType): List<AdventureEntity>
	fun findByDungeonMasterId(dmId: UUID): List<AdventureEntity>
	fun findByTitleContainingIgnoreCase(keyword: String): List<AdventureEntity>
}

// --------------------------- GameSession ------------------------------------
interface GameSessionRepository : JpaRepository<GameSessionEntity, UUID> {
	/** Возвращает сессии, начинающиеся позже указанного времени (по умолчанию – сейчас). */
	@Query("""
        select s from GameSessionEntity s
        where s.startTime >= :from
        order by s.startTime asc
    """)
	fun findUpcoming(@Param("from") from: OffsetDateTime = OffsetDateTime.now()): List<GameSessionEntity>
	
	fun findByAdventureId(adventureId: UUID): List<GameSessionEntity>
}

// --------------------------- Signup -----------------------------------------
interface AdventureSignupRepository : JpaRepository<AdventureSignupEntity, UUID> {
	fun countByAdventureId(adventureId: UUID): Long
	
	fun existsByAdventureIdAndUserId(adventureId: UUID, userId: UUID): Boolean
	
	fun findByAdventureIdAndStatus(adventureId: UUID, status: String): List<AdventureSignupEntity>
}

// --------------------------- Currency ---------------------------------------
interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, String> {
	// Возвращает ratio для нужного ISO-кода валюты
	@Query("""select c.ratio from CurrencyRateEntity c where c.currency = :code""")
	fun findRatioByCurrency(@Param("code") currency: String): Int?
}
