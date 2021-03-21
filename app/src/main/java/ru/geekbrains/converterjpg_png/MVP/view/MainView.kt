package ru.geekbrains.converterjpg_png.MVP.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import ru.geekbrains.converterjpg_png.MVP.model.Image

@AddToEndSingle
interface MainView: MvpView{
    @Skip
    fun getImage()

    fun showConvertStatus(status: String?)
    @Skip
    fun showSavingStatus(status: String)

    fun enableSaveBtn(enable: Boolean)
    fun disableSaveBtn(disable: Boolean)
    fun enableBtnOk(enable: Boolean)
    fun disableBtnOk(disable: Boolean)

    fun showImage(image: Image)
    fun enableConvertBtn(enable: Boolean)
    fun disableConvertBtn(disable: Boolean)

    fun showAlert()
    fun dismissAlert()
}