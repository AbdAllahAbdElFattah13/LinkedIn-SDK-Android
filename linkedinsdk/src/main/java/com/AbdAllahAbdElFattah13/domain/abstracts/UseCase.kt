package com.AbdAllahAbdElFattah13.domain.abstracts

abstract class UseCase<in Input, Output> {

    abstract fun run(input: Input?): Output
}