package org.affidtech.dnd.admin.service

import org.affidtech.dnd.admin.exception.AlreadyExistsException
import org.affidtech.dnd.admin.exception.NotFoundException
import org.affidtech.dnd.admin.repo.CurrencyRateRepository
import org.affidtech.dnd.admin.web.CurrencyRateMapper
import org.affidtech.dnd.admin.web.dto.CurrencyRateCreateDto
import org.affidtech.dnd.admin.web.dto.CurrencyRateDto
import org.affidtech.dnd.admin.web.dto.CurrencyRatePatchDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class CurrencyRateService(
	private val currencyRateRepository: CurrencyRateRepository,
	private val currencyRateMapper: CurrencyRateMapper
) {
	
	fun getAll(): List<CurrencyRateDto> =
		currencyRateRepository.findAll().map(currencyRateMapper::toDto)
	
	fun getByCurrency(currency: String): CurrencyRateDto =
		currencyRateRepository.findByCurrency(currency)
			?.let(currencyRateMapper::toDto)
			?: throw NotFoundException("Currency $currency not found")
	
	@Transactional
	fun create(dto: CurrencyRateCreateDto): CurrencyRateDto {
		if (currencyRateRepository.existsByCurrency(dto.currency))
			throw AlreadyExistsException("Currency ${dto.currency} already exists")
		val entity = currencyRateMapper.toEntity(dto).copy(updatedAt = OffsetDateTime.now())
		val saved = currencyRateRepository.save(entity)
		return currencyRateMapper.toDto(saved)
	}
	
	@Transactional
	fun patch(currency: String, dto: CurrencyRatePatchDto): CurrencyRateDto {
		val entity = currencyRateRepository.findByCurrency(currency)
			?: throw NotFoundException("Currency $currency not found")
		currencyRateMapper.updateEntityFromPatch(dto, entity)
		entity.updatedAt = OffsetDateTime.now() // обновлять timestamp при изменении
		val saved = currencyRateRepository.save(entity)
		return currencyRateMapper.toDto(saved)
	}
	
	@Transactional
	fun delete(currency: String) {
		if (!currencyRateRepository.existsByCurrency(currency)) throw NotFoundException("Currency $currency not found")
		currencyRateRepository.deleteById(currency)
	}
}
