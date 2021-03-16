package com.mrkaz.tokoin.data.exception

class InvalidInputException(message: String) : Exception(message)
class ConflictException(message: String) : Exception("Conflict data : $message")
class NetworkException(message: String) : Exception(message)