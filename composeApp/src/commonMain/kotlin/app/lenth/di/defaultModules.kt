package app.lenth.di

import app.lenth.Greeting
import org.koin.dsl.module

val defaultModules = module {
    single { Greeting() }
}