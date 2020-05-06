package com.AbdAllahAbdElFattah13.domain.usecases

import com.AbdAllahAbdElFattah13.domain.utils.Executors

abstract class UseCase<in Input, Output> constructor(
        private val executors: Executors
) {

    abstract fun run(input: Input?): Output
}