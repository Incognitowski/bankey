package parameters

class ParameterAPI(private val mParameterRepository: ParameterRepository) {

    fun create(aParameterEntity: ParameterEntity) = mParameterRepository.create(aParameterEntity)

    fun update(aParameterEntity: ParameterEntity) = mParameterRepository.update(aParameterEntity)

    fun findLatest(): ParameterEntity? = mParameterRepository.findLatest()

}