package org.univesp.natalagapebackend.handler

class MaxContributionException : RuntimeException {
    override val message: String
    val status: Int

    constructor(message: String, status: Int) {
        this.message = message
        this.status = status
    }

    constructor(message: String) : this(message, 403)
}