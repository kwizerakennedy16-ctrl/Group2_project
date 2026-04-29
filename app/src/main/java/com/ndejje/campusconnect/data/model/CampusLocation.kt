package com.ndejje.campusconnect.data.model

/**
 * Represents a physical facility or landmark on Ndejje University campuses.
 * Not persisted in Room — loaded from a local static data source.
 */
data class CampusLocation(
    val id: Int,
    val name: String,
    val description: String,
    val building: String,
    val floor: String,
    val category: LocationCategory
)

enum class LocationCategory {
    ALL,
    ACADEMIC,
    ADMIN,
    SOCIAL,
    HEALTH
}

/**
 * Represents a university contact or a downloadable PDF student guide.
 */
data class UniversityResource(
    val id: Int,
    val title: String,
    val subtitle: String,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val pdfUrl: String? = null,
    val resourceType: ResourceType
)

enum class ResourceType {
    CONTACT,
    GUIDE
}
