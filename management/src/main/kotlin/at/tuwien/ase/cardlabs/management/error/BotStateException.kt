package at.tuwien.ase.cardlabs.management.error

// An exception that is thrown when modifying a bot state and the state logic is not followed
class BotStateException(message: String?) : Exception(message)
