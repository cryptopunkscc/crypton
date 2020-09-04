package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.IQ
import cc.cryptopunks.crypton.context.Jingle
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.interactor.removeSessionScope

fun SessionScope.createSessionInitiate(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    contentSenders: Jingle.Content.Senders,
    contentDescription: Jingle.Content.Description,
    contentTransport: Jingle.Content.Transport
) = Jingle(
    from = address,
    to = recipient,
    action = Jingle.Action.session_initiate,
    initiator = Resource(address),
    sessionId = sessionId,
    contents = listOf(
        Jingle.Content(
            name = contentName,
            senders = contentSenders,
            description = contentDescription,
            creator = contentCreator,
            transport = contentTransport
        )
    )
)

fun SessionScope.createSessionInitiateFileOffer(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    description: Jingle.Content.Description,
    transport: Jingle.Content.Transport
): Jingle = createSessionInitiate(
    recipient,
    sessionId,
    contentCreator,
    contentName,
    Jingle.Content.Senders.initiator,
    description,
    transport
)

fun SessionScope.sendSessionInitiateFileOffer(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    description: Jingle.Content.Description,
    transport: Jingle.Content.Transport
): IQ? {
    val jingle: Jingle = createSessionInitiateFileOffer(
        recipient,
        sessionId,
        contentCreator,
        contentName,
        description,
        transport
    )
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.sendSessionInitiate(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    contentSenders: Jingle.Content.Senders,
    description: Jingle.Content.Description,
    transport: Jingle.Content.Transport
): IQ? {
    val jingle: Jingle = createSessionInitiate(
        recipient, sessionId, contentCreator, contentName, contentSenders,
        description, transport
    )
    return createStanzaCollectorAndSend(jingle).nextResult()
}

fun SessionScope.createSessionAccept(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    contentSenders: Jingle.Content.Senders,
    description: Jingle.Content.Description,
    transport: Jingle.Content.Transport
) = Jingle(
    responder = Resource(address),
    action = Jingle.Action.session_accept,
    sessionId = sessionId,
    contents = listOf(
        Jingle.Content(
            creator = contentCreator,
            name = contentName,
            senders = contentSenders,
            description = description,
            transport = transport
        )
    ),
    from = address,
    to = recipient
)

fun SessionScope.sendSessionAccept(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    contentSenders: Jingle.Content.Senders,
    description: Jingle.Content.Description,
    transport: Jingle.Content.Transport
): IQ? {
    val jingle: Jingle = createSessionAccept(
        recipient, sessionId, contentCreator, contentName, contentSenders,
        description, transport
    )
    return createStanzaCollectorAndSend(jingle).nextResult()
}

fun SessionScope.createSessionTerminate(
    recipient: Resource,
    sessionId: String,
    reason: Jingle.Reason
) = Jingle(
    action = Jingle.Action.session_terminate,
    sessionId = sessionId,
    reason = reason,
    from = address,
    to = recipient
)

fun SessionScope.createSessionTerminateDecline(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.decline)
}

fun SessionScope.sendSessionTerminateDecline(recipient: Resource, sessionId: String): IQ? {
    val jingle: Jingle =
        createSessionTerminateDecline(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateSuccess(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.success)
}

fun SessionScope.sendSessionTerminateSuccess(recipient: Resource, sessionId: String): IQ? {
    val jingle: Jingle =
        createSessionTerminateSuccess(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateBusy(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.busy)
}

fun SessionScope.sendSessionTerminateBusy(recipient: Resource, sessionId: String): IQ? {
    val jingle: Jingle =
        createSessionTerminateBusy(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateAlternativeSession(
    recipient: Resource,
    sessionId: String,
    altSessionId: String
): Jingle {
    return createSessionTerminate(
        recipient,
        sessionId,
        Jingle.Reason.AlternativeSession(altSessionId)
    )
}

fun SessionScope.sendSessionTerminateAlternativeSession(
    recipient: Resource,
    sessionId: String,
    altSessionId: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateAlternativeSession(recipient, sessionId, altSessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateCancel(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.cancel)
}

fun SessionScope.sendSessionTerminateCancel(
    recipient: Resource,
    sessionId: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateCancel(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateContentCancel(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String
) = Jingle(
    action = Jingle.Action.session_terminate,
    sessionId = sessionId,
    contents = listOf(
        Jingle.Content(
            creator = contentCreator,
            name = contentName
        )
    ),
    from = address,
    to = recipient
)

fun SessionScope.sendSessionTerminateContentCancel(
    recipient: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateContentCancel(recipient, sessionId, contentCreator, contentName)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateUnsupportedTransports(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.unsupported_transports)
}

fun SessionScope.sendSessionTerminateUnsupportedTransports(
    recipient: Resource,
    sessionId: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateUnsupportedTransports(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateFailedTransport(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.failed_transport)
}

fun SessionScope.sendSessionTerminateFailedTransport(recipient: Resource, sessionId: String): IQ? {
    val jingle: Jingle =
        createSessionTerminateFailedTransport(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateUnsupportedApplications(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(
        recipient,
        sessionId,
        Jingle.Reason.unsupported_applications
    )
}

fun SessionScope.sendSessionTerminateUnsupportedApplications(
    recipient: Resource,
    sessionId: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateUnsupportedApplications(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateFailedApplication(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.failed_application)
}

fun SessionScope.sendSessionTerminateFailedApplication(
    recipient: Resource,
    sessionId: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateFailedApplication(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createSessionTerminateIncompatibleParameters(
    recipient: Resource,
    sessionId: String
): Jingle {
    return createSessionTerminate(recipient, sessionId, Jingle.Reason.incompatible_parameters)
}

fun SessionScope.sendSessionTerminateIncompatibleParameters(
    recipient: Resource,
    sessionId: String
): IQ? {
    val jingle: Jingle =
        createSessionTerminateIncompatibleParameters(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.sendContentRejectFileNotAvailable(
    recipient: Resource,
    sessionId: String,
    description: Jingle.Content.Description
): IQ? {
    return null //TODO Later
}

fun SessionScope.createSessionPing(
    recipient: Resource,
    sessionId: String
) = Jingle(
    sessionId = sessionId,
    action = Jingle.Action.session_info,
    from = address,
    to = recipient
)

fun SessionScope.sendSessionPing(recipient: Resource, sessionId: String): IQ? {
    val jingle: Jingle =
        createSessionPing(recipient, sessionId)
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createAck(jingle: Jingle?): IQ? {
    return IQ.createResultIQ(jingle)
}

fun SessionScope.sendAck(jingle: Jingle?) {
    sendStanza(createAck(jingle))
}

fun SessionScope.createTransportReplace(
    recipient: Resource,
    initiator: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    transport: Jingle.Content.Transport
) = Jingle(
    initiator = initiator,
    sessionId = sessionId,
    action = Jingle.Action.transport_replace,
    contents = listOf(
        Jingle.Content(
            name = contentName,
            creator = contentCreator,
            transport = transport
        )
    ),
    to = recipient,
    from = address
)

fun SessionScope.sendTransportReplace(
    recipient: Resource,
    initiator: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    transport: Jingle.Content.Transport
): IQ? {
    val jingle: Jingle = createTransportReplace(
        recipient,
        initiator,
        sessionId,
        contentCreator,
        contentName,
        transport
    )
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createTransportAccept(
    recipient: Resource,
    initiator: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    transport: Jingle.Content.Transport
) = Jingle(
    initiator = initiator,
    sessionId = sessionId,
    action = Jingle.Action.content_accept,
    contents = listOf(
        Jingle.Content(
            name = contentName,
            creator = contentCreator,
            transport = transport
        )
    ),
    to = recipient,
    from = address
)


fun SessionScope.sendTransportAccept(
    recipient: Resource,
    initiator: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    transport: Jingle.Content.Transport
): IQ? {
    val jingle: Jingle = createTransportAccept(
        recipient,
        initiator,
        sessionId,
        contentCreator,
        contentName,
        transport
    )
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createTransportReject(
    recipient: Resource,
    initiator: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    transport: Jingle.Content.Transport
) = Jingle(
    initiator = initiator,
    sessionId = sessionId,
    action = Jingle.Action.transport_reject,
    contents = listOf(
        Jingle.Content(
            name = contentName,
            creator = contentCreator,
            transport = transport
        )
    ),
    to = recipient,
    from = address
)


fun SessionScope.sendTransportReject(
    recipient: Resource,
    initiator: Resource,
    sessionId: String,
    contentCreator: Jingle.Content.Creator,
    contentName: String,
    transport: Jingle.Content.Transport
): IQ? {
    val jingle: Jingle = createTransportReject(
        recipient,
        initiator,
        sessionId,
        contentCreator,
        contentName,
        transport
    )
    return createStanzaCollectorAndSend(jingle).nextResultOrThrow()
}

fun SessionScope.createErrorUnknownSession(request: Jingle?): IQ? {
    val error: org.jivesoftware.smack.packet.StanzaError.Builder = StanzaError.getBuilder()
    error.setCondition(StanzaError.Condition.item_not_found)
        .addExtension(JingleError.UNKNOWN_SESSION)
    return IQ.createErrorResponse(request, error)
}

fun SessionScope.sendErrorUnknownSession(request: Jingle?) {
    sendStanza(createErrorUnknownSession(request))
}

fun SessionScope.createErrorUnknownInitiator(request: Jingle?): IQ? {
    return IQ.createErrorResponse(request, StanzaError.Condition.service_unavailable)
}

fun SessionScope.sendErrorUnknownInitiator(request: Jingle?) {
    sendStanza(createErrorUnknownInitiator(request))
}

fun SessionScope.createErrorUnsupportedInfo(request: Jingle?): IQ? {
    val error: org.jivesoftware.smack.packet.StanzaError.Builder = StanzaError.getBuilder()
    error.setCondition(StanzaError.Condition.feature_not_implemented)
        .addExtension(JingleError.UNSUPPORTED_INFO)
    return IQ.createErrorResponse(request, error)
}

fun SessionScope.sendErrorUnsupportedInfo(request: Jingle?) {
    sendStanza(createErrorUnsupportedInfo(request))
}

fun SessionScope.createErrorTieBreak(request: Jingle?): IQ? {
    val error: org.jivesoftware.smack.packet.StanzaError.Builder = StanzaError.getBuilder()
    error.setCondition(StanzaError.Condition.conflict)
        .addExtension(JingleError.TIE_BREAK)
    return IQ.createErrorResponse(request, error)
}

fun SessionScope.sendErrorTieBreak(request: Jingle?) {
    sendStanza(createErrorTieBreak(request))
}

fun SessionScope.createErrorOutOfOrder(request: Jingle?): IQ? {
    val error: org.jivesoftware.smack.packet.StanzaError.Builder = StanzaError.getBuilder()
    error.setCondition(StanzaError.Condition.unexpected_request)
        .addExtension(JingleError.OUT_OF_ORDER)
    return IQ.createErrorResponse(request, error)
}

fun SessionScope.sendErrorOutOfOrder(request: Jingle?) {
    sendStanza(createErrorOutOfOrder(request))
}

fun SessionScope.createErrorMalformedRequest(request: Jingle?): IQ? {
    return IQ.createErrorResponse(request, StanzaError.Condition.bad_request)
}

fun SessionScope.sendErrorMalformedRequest(request: Jingle?) {
    sendStanza(createErrorMalformedRequest(request))
}
