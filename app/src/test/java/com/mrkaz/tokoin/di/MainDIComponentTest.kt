package com.mrkaz.tokoin.di

fun configureTestAppComponent(baseApi: String) = listOf(
    MockWebServerDIPTest,
    configureNetworkModuleForTest(baseApi),
    DatabaseDITest,
    UseCaseDependency,
    ViewModelDependency,
    RepositoryDependency,
    UtilsDependency,
)

fun configureTestAppWithDatabaseComponent() = listOf(
    DatabaseDITest,
    UseCaseDependency,
    RepositoryDITest,
    UtilsDITest,
)



