package at.tuwien.ase.cardlabs.management.util

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.Collections.emptyList

@JsonIgnoreProperties("empty")
class RestResponsePage<T> : PageImpl<T> {
    constructor(
        @JsonProperty("content") content: List<T>,
        @JsonProperty("number") number: Int,
        @JsonProperty("size") size: Int,
        @JsonProperty("totalElements") totalElements: Long,
        @JsonProperty("pageable") pageable: JsonNode,
        @JsonProperty("last") last: Boolean,
        @JsonProperty("totalPages") totalPages: Int,
        @JsonProperty("sort") sort: JsonNode,
        @JsonProperty("first") first: Boolean,
        @JsonProperty("numberOfElements") numberOfElements: Int,
    ) : super(content, PageRequest.of(number, size), totalElements)

    constructor(content: List<T>, pageable: Pageable, total: Long) : super(content, pageable, total)

    constructor(content: List<T>) : super(content)

    constructor() : super(emptyList())
}
