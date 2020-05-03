package client.common.data

import io.ktor.client.HttpClient
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.AuthenticationRequestDto
import ru.memebattle.common.dto.AuthenticationResponseDto
import ru.memebattle.common.dto.game.MemeModel
import ru.memebattle.common.model.RatingModel

@Suppress("FunctionName")
fun MemeClient(tokenSource: TokenSource): HttpClient = HttpClient {
    Json {
        serializer = KotlinxSerializer()
    }
    Auth {
        bearer(tokenSource)
    }
    Logging {
        logger = Logger.DEFAULT
    }

    install(WebSockets)
}

internal suspend fun HttpClient.signUp(authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto =
    post("${baseUrl()}registration") {
        contentType(ContentType.Application.Json)
        body = authenticationRequestDto
    }

internal suspend fun HttpClient.signIn(authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto =
    post("${baseUrl()}authentication") {
        contentType(ContentType.Application.Json)
        body = authenticationRequestDto
    }

internal suspend fun HttpClient.getRating(): List<RatingModel> =
    get("${baseUrl()}rating")

internal suspend fun HttpClient.getChillMemes(mode: GameMode): List<MemeModel> =
    get("${baseUrl()}chill?gameMode=${mode.name}")