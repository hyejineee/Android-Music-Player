package com.hyejineee.musicapp.util

import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

object Dispatcher {
    val onAction: FlowableProcessor<Action> = PublishProcessor.create()

    fun dispatch(action:Action){
        onAction.onNext(action)
    }
}