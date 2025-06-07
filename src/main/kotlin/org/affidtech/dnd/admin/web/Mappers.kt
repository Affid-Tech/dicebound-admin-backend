package org.affidtech.dnd.admin.web

import org.affidtech.dnd.admin.domain.*
import org.affidtech.dnd.admin.web.dto.*
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface UserMapper {
	@Mapping(target = "roles", ignore = true) // заполняем руками, см. выше
	fun toDto(entity: UserEntity): UserDto
	
	fun toEntity(dto: UserCreateDto): UserEntity
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateEntityFromPatch(dto: UserPatchDto, @MappingTarget entity: UserEntity)
}

@Mapper(
	componentModel = "spring",
	uses = [UserMapper::class, GameSessionMapper::class, AdventureSignupMapper::class]
)
interface AdventureMapper {
	
	fun toDto(entity: AdventureEntity): AdventureDto
	
	fun toEntity(dto: AdventureCreateDto, @Context dm: UserEntity): AdventureEntity
	// dm — заранее найденный dungeonMaster
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateEntityFromPatch(dto: AdventurePatchDto, @MappingTarget entity: AdventureEntity, @Context dm: UserEntity?)
	// если dungeonMasterId в patch — передаём нового dm, иначе null
}

@Mapper(componentModel = "spring")
interface GameSessionMapper {
	
	fun toDto(entity: GameSessionEntity): GameSessionDto
	
	fun toEntity(dto: GameSessionCreateDto, @Context adventure: AdventureEntity): GameSessionEntity
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateEntityFromPatch(dto: GameSessionPatchDto, @MappingTarget entity: GameSessionEntity)
}

@Mapper(componentModel = "spring", uses = [UserMapper::class])
interface AdventureSignupMapper {
	
	fun toDto(entity: AdventureSignupEntity): AdventureSignupDto
	
	fun toEntity(dto: AdventureSignupCreateDto, @Context adventure: AdventureEntity, @Context user: UserEntity): AdventureSignupEntity
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateEntityFromPatch(dto: AdventureSignupPatchDto, @MappingTarget entity: AdventureSignupEntity)
}

@Mapper(componentModel = "spring")
interface CurrencyRateMapper {
	
	fun toDto(entity: CurrencyRateEntity): CurrencyRateDto
	
	fun toEntity(dto: CurrencyRateCreateDto): CurrencyRateEntity
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateEntityFromPatch(dto: CurrencyRatePatchDto, @MappingTarget entity: CurrencyRateEntity)
}
