package org.affidtech.dnd.admin.web

import org.affidtech.dnd.admin.domain.*
import org.affidtech.dnd.admin.web.dto.*
import org.mapstruct.*
import java.util.UUID


@Mapper(componentModel = "spring")
interface UserMapper {
	@Mapping(target = "roles", expression = "java(java.util.Collections.emptyList())") // заполняем руками
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
	
	@Mapping(target = "sessions", expression = "java(new ArrayList())") // заполняем руками
	@Mapping(target = "signups", expression = "java(new ArrayList())") // заполняем руками
	@Mapping(target = "dungeonMaster", expression = "java(dm)") // dm — заранее найденный dungeonMaster
	fun toEntity(dto: AdventureCreateDto, @Context dm: UserEntity): AdventureEntity
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "dungeonMaster", source = "dungeonMasterId", conditionExpression = "java(dm != null)") // если dungeonMasterId в patch — передаём нового dm, иначе null
	fun updateEntityFromPatch(dto: AdventurePatchDto, @MappingTarget entity: AdventureEntity, @Context dm: UserEntity?)
	
	fun provideDungeonMaster(dungeonMasterId: UUID, @Context dm: UserEntity): UserEntity{
		return dm
	}
}

@Mapper(componentModel = "spring")
interface GameSessionMapper {
	@Mapping(source = "adventure.id", target = "adventureId")
	fun toDto(entity: GameSessionEntity): GameSessionDto
	
	@Mapping(target = "adventure", expression = "java(adventure)")
	fun toEntity(dto: GameSessionCreateDto, @Context adventure: AdventureEntity): GameSessionEntity
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	fun updateEntityFromPatch(dto: GameSessionPatchDto, @MappingTarget entity: GameSessionEntity)
}

@Mapper(componentModel = "spring", uses = [UserMapper::class])
interface AdventureSignupMapper {
	@Mapping(source = "adventure.id", target = "adventureId")
	fun toDto(entity: AdventureSignupEntity): AdventureSignupDto
	
	
	@Mapping(target = "adventure", expression = "java(adventure)")
	@Mapping(target = "user", expression = "java(user)")
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
