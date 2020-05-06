package com.AbdAllahAbdElFattah13.linkedinsdk.domain.abstracts

abstract class UseCase<in Input, Output> {

    abstract fun run(input: Input?): Output
}