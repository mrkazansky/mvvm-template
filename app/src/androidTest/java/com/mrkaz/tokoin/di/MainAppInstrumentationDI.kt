package com.mrkaz.tokoin.di


fun generateTestAppComponent(baseApi: String) = listOf(
    configureNetworkForInstrumentationTest(baseApi),
    UseCaseDependency,
    MockWebServerInstrumentationTest,
    RepositoryDependency,
    ViewModelDependency,
    UtilsInstrumentTest,
    DatabaseInstrumentTest
)