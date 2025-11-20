package org.affidtech.dnd.admin.repo

import jakarta.persistence.criteria.Predicate
import org.affidtech.dnd.admin.domain.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.OffsetDateTime
import java.util.*

// --------------------------- Core -------------------------------------------
interface UserRepository : JpaRepository<UserEntity, UUID>{
	@Query(
		"""
        select u from UserEntity u
        where (:keyword is null or :keyword = ''
               or lower(u.name) like lower(concat('%', :keyword, '%'))
               or lower(u.email) like lower(concat('%', :keyword, '%')))
        """
	)
	fun search(keyword: String?, pageable: Pageable): Page<UserEntity>
	
}

interface PlayerProfileRepository : JpaRepository<PlayerProfileEntity, UUID> {
	fun existsByUser(user: UserEntity): Boolean
	fun deleteByUserId(userId: UUID)
	
	@Query("select p.user from PlayerProfileEntity p")
	fun findUsers(pageable: Pageable): Page<UserEntity>
	
	@Query(
		"""
        select p.user from PlayerProfileEntity p
        where (:keyword is null or :keyword = ''
               or lower(p.user.name) like lower(concat('%', :keyword, '%'))
               or lower(p.user.email) like lower(concat('%', :keyword, '%')))
        """
	)
	fun searchUsers(keyword: String?, pageable: Pageable): Page<UserEntity>
	
}

interface DungeonMasterProfileRepository : JpaRepository<DungeonMasterProfileEntity, UUID> {
	fun existsByUser(user: UserEntity): Boolean
	fun deleteByUserId(userId: UUID)
	
	@Query("select p.user from DungeonMasterProfileEntity p")
	fun findUsers(pageable: Pageable): Page<UserEntity>
}

interface AdminProfileRepository : JpaRepository<AdminProfileEntity, UUID> {
	fun existsByUser(user: UserEntity): Boolean
	fun deleteByUserId(userId: UUID)
	
	@Query("select p.user from AdminProfileEntity p")
	fun findUsers(pageable: Pageable): Page<UserEntity>
}


// --------------------------- Adventure --------------------------------------
interface AdventureRepository : JpaRepository<AdventureEntity, UUID>, JpaSpecificationExecutor<AdventureEntity> {

}

object AdventureSpecifications {
	
	fun byFilters(
		types: Collection<AdventureType>?,
		statuses: Collection<AdventureStatus>?,
		dungeonMasterIds: Collection<UUID>?
	): Specification<AdventureEntity> {
		return Specification { root, _, cb ->
			val predicates = mutableListOf<Predicate>()
			
			if (!types.isNullOrEmpty()) {
				predicates += root.get<AdventureType>("type").`in`(types)
			}
			
			if (!statuses.isNullOrEmpty()) {
				predicates += root.get<AdventureStatus>("status").`in`(statuses)
			}
			
			if (!dungeonMasterIds.isNullOrEmpty()) {
				// по полю dungeonMaster.id
				val dmPath = root.get<UserEntity>("dungeonMaster").get<UUID>("id")
				predicates += dmPath.`in`(dungeonMasterIds)
			}
			
			if (predicates.isEmpty()) {
				cb.conjunction() // без фильтров — всё
			} else {
				cb.and(*predicates.toTypedArray())
			}
		}
	}
}

// --------------------------- GameSession ------------------------------------
interface GameSessionRepository : JpaRepository<GameSessionEntity, UUID> {
	/** Возвращает сессии, начинающиеся позже указанного времени (по умолчанию – сейчас). */
	@Query(
		"""
        select s from GameSessionEntity s
        where s.startTime >= :from
        order by s.startTime asc
    """
	)
	fun findUpcoming(@Param("from") from: OffsetDateTime = OffsetDateTime.now()): List<GameSessionEntity>
	
	fun findAllByAdventureId(adventureId: UUID): List<GameSessionEntity>
}

// --------------------------- Signup -----------------------------------------
interface AdventureSignupRepository : JpaRepository<AdventureSignupEntity, UUID> {
	fun findAllByAdventureId(adventureId: UUID): List<AdventureSignupEntity>
	fun findAllByUserId(userId: UUID): List<AdventureSignupEntity>
	fun existsByAdventureIdAndUserId(adventureId: UUID, userId: UUID): Boolean
}

// --------------------------- Currency ---------------------------------------
interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, String> {
	fun findByCurrency(currency: String): CurrencyRateEntity?
	fun existsByCurrency(currency: String): Boolean
}
