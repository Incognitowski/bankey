package parameter

import framework.exception.EntityNotFoundException

class ParameterAPI(private val mParameterRepository: ParameterRepository) {

    fun create(aParameterEntity: ParameterEntity) = mParameterRepository.create(aParameterEntity)

    fun update(aParameterEntity: ParameterEntity) {
        mParameterRepository.findById(aParameterEntity.parameterId)
            ?: throw EntityNotFoundException("Parameter with ID ${aParameterEntity.parameterId} not found.")
        mParameterRepository.update(aParameterEntity)
    }

    fun findLatest(): ParameterEntity? = mParameterRepository.findLatest()

}