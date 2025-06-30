package com.domain.model


// Custom LocalDate serializer (if you're using Kotlinx Serialization directly with LocalDate)
// You'd put this in a common utility package if used widely.
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateSerializer : KSerializer<LocalDate> {
	@RequiresApi(Build.VERSION_CODES.O)
	private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "YYYY-MM-DD"
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
	@RequiresApi(Build.VERSION_CODES.O)
	override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString(), formatter)
	@RequiresApi(Build.VERSION_CODES.O)
	override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.format(formatter))
}