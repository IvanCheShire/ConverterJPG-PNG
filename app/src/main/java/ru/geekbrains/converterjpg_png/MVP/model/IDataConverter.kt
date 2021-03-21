package ru.geekbrains.converterjpg_png.MVP.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


interface IDataConverter {
    fun convert(image: Image?): Single<ByteArray>
    fun saveImage(image: Image?): Completable
}