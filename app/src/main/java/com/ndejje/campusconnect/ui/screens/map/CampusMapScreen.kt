package com.ndejje.campusconnect.ui.screens.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ndejje.campusconnect.R
import com.ndejje.campusconnect.data.model.CampusLocation
import com.ndejje.campusconnect.data.model.LocationCategory

// ── Static campus data ────────────────────────────────────────────────────────
val ndejjeCampusLocations = listOf(
    CampusLocation(1, "Faculty of Science and Computing", "Offers BCS, BIT, BIS programmes", "Senate Building", "Ground Floor", LocationCategory.ACADEMIC),
    CampusLocation(2, "Faculty of Engineering", "Civil, Electrical and Mechanical Engineering", " Engineering Block", "Ground Floor", LocationCategory.ACADEMIC),
    CampusLocation(3, "Computer Lab 1", "General computing lab – 40 workstations", "Block B", "1st Floor", LocationCategory.ACADEMIC),
    CampusLocation(4, "Computer Lab 3 (Mobile Dev)", "Dedicated to BCS 2201 mobile programming sessions", "Block B", "Ground Floor", LocationCategory.ACADEMIC),
    CampusLocation(5, "Lecture Hall 1", "Large capacity hall – seats 200", "Main Building", "Ground Floor", LocationCategory.ACADEMIC),
    CampusLocation(6, "Lecture Hall 4", "Medium hall – seats 80", "Main Building", "1st Floor", LocationCategory.ACADEMIC),
    CampusLocation(7, "University Library", "Open 07:00–22:00 during revision. WiFi enabled", "Library Block", "Ground Floor", LocationCategory.ACADEMIC),
    CampusLocation(8, "Academic Registrar's Office", "Exam results, transcripts, admission letters", "Admin Block C", "Ground Floor", LocationCategory.ADMIN),
    CampusLocation(9, "Bursar's Office", "Fees payment and financial queries", "Admin Block C", "Ground Floor", LocationCategory.ADMIN),
    CampusLocation(10, "Vice Chancellor's Office", "Senior administration and governance", "Admin Block", "2nd Floor", LocationCategory.ADMIN),
    CampusLocation(11, "ICT Department", "Internet support, student email, Moodle help", "Block B", "2nd Floor", LocationCategory.ADMIN),
    CampusLocation(12, "Student Guild Office", "Student representation and welfare", "Student Centre", "Ground Floor", LocationCategory.SOCIAL),
    CampusLocation(13, "Main Cafeteria", "Hot meals served 07:00–20:00", "Student Centre", "Ground Floor", LocationCategory.SOCIAL),
    CampusLocation(14, "Sports Grounds", "Football, basketball and athletics", "Grounds", "Outdoor", LocationCategory.SOCIAL),
    CampusLocation(15, "University Health Centre", "Free medical consultation for students and staff", "Health Block", "Ground Floor", LocationCategory.HEALTH),
    CampusLocation(16, "Campus Clinic – Bombo", "Branch clinic for Bombo-side residents", "Bombo Annex", "Ground Floor", LocationCategory.HEALTH),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusMapScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(LocationCategory.ALL) }

    val filteredLocations = remember(searchQuery, selectedCategory) {
        ndejjeCampusLocations.filter { location ->
            val matchesQuery = searchQuery.isBlank() ||
                    location.name.contains(searchQuery, ignoreCase = true) ||
                    location.description.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == LocationCategory.ALL ||
                    location.category == selectedCategory
            matchesQuery && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_title_map),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimensionResource(R.dimen.spacing_md))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))

            // ── Search bar ─────────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.label_search_location)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))

            // ── Category filter chips ──────────────────────────────────
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
            ) {
                items(LocationCategory.values()) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(locationCategoryLabel(category)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_sm)))

            Text(
                text = "${filteredLocations.size} ${stringResource(R.string.label_facilities)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))

            // ── Locations list ─────────────────────────────────────────
            if (filteredLocations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.label_no_results),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.spacing_sm)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_sm))
                ) {
                    items(
                        items = filteredLocations,
                        key = { location -> location.id }
                    ) { location ->
                        LocationCard(location = location)
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationCard(location: CampusLocation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_md)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = locationIcon(location.category),
                contentDescription = stringResource(R.string.cd_location_icon),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_lg))
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_md)))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = location.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xs)))
                Text(
                    text = "${stringResource(R.string.label_building)}: ${location.building}  •  ${stringResource(R.string.label_floor)}: ${location.floor}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun locationCategoryLabel(category: LocationCategory) = when (category) {
    LocationCategory.ALL -> "All"
    LocationCategory.ACADEMIC -> "Academic"
    LocationCategory.ADMIN -> "Admin"
    LocationCategory.SOCIAL -> "Social"
    LocationCategory.HEALTH -> "Health"
}

private fun locationIcon(category: LocationCategory): ImageVector = when (category) {
    LocationCategory.ACADEMIC -> Icons.Filled.MenuBook
    LocationCategory.ADMIN -> Icons.Filled.AccountBalance
    LocationCategory.SOCIAL -> Icons.Filled.FitnessCenter
    LocationCategory.HEALTH -> Icons.Filled.LocalHospital
    LocationCategory.ALL -> Icons.Filled.LocationOn
}
