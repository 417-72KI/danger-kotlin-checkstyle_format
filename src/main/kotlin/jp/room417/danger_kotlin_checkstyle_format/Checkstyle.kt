package jp.room417.danger_kotlin_checkstyle_format

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(namespace = "checkstyle")
internal data class Checkstyle(
    @field:JsonProperty("file")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val files: List<File> = emptyList()
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class File(
        @field:JacksonXmlProperty val name: String = "",
        @field:JsonProperty("error")
        @field:JacksonXmlElementWrapper(useWrapping = false)
        val errors: List<Error> = emptyList()
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Error(
            @field:JacksonXmlProperty val line: String = "",
            @field:JacksonXmlProperty val column: String = "",
            @field:JacksonXmlProperty val severity: String = "",
            @field:JacksonXmlProperty val message: String = "",
            @field:JacksonXmlProperty val source: String = ""
        )
    }
}
