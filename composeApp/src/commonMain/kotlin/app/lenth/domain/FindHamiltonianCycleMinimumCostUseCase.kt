package app.lenth.domain

import app.lenth.data.GeoCodingRepository
import app.lenth.ui.history.models.OptimalRouteUi
import kotlin.math.*

data class PlaceUi(val name: String, val lat: Double, val lng: Double)

class FindHamiltonianCycleMinimumCostUseCase(private val geoCodingRepository: GeoCodingRepository) {
    // TODO Return domain instead of ui...
    suspend operator fun invoke(places: List<String>): OptimalRouteUi {
        // Step 1: Geocode the places to get coordinates
        val geoCoding = places.filter { it.isNotBlank() && it.isNotEmpty() }.map {
            geoCodingRepository.getGeoCoding(it)
        }

        // Step 2: Generate the distance matrix
        val distanceMatrix = generateDistanceMatrix(geoCoding.map { it.lat to it.lng })

        // Step 3: Solve the TSP and get the optimal path
        val (minCost, pathIndices) = heldKarp(distanceMatrix)

        // Step 4: Map the path indices back to PlaceWithCoordinates
        val orderedPath = pathIndices.map { geoCoding[it] }.map { PlaceUi(it.name, it.lat, it.lng) } // TODO Use PlaceDomain

        // Step 5: Return OptimalRouteUi with the full path including coordinates
        return OptimalRouteUi(distance = minCost, path = orderedPath, mapImage = null)
    }

    fun toRadians(deg: Double): Double = deg / 180.0 * PI

    fun generateDistanceMatrix(locations: List<Pair<Double, Double>>): Array<DoubleArray> {
        val n = locations.size
        val matrix = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (i == j) {
                    matrix[i][j] = 0.0 // Distance to self is 0
                } else {
                    matrix[i][j] = haversine(
                        locations[i].first, locations[i].second,
                        locations[j].first, locations[j].second
                    )
                }
            }
        }
        return matrix
    }

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Radius of Earth in kilometers
        val dLat = toRadians(lat2 - lat1)
        val dLon = toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Distance in kilometers
    }

    fun heldKarp(distanceMatrix: Array<DoubleArray>): Pair<Double, List<Int>> {
        val n = distanceMatrix.size
        val memo = Array(1 shl n) { DoubleArray(n) { Double.MAX_VALUE } } // Initialize with infinity
        val parent = Array(1 shl n) { IntArray(n) { -1 } } // Initialize with -1

        // Base case: Starting from every city except the start city (index 0)
        for (i in 1 until n) {
            memo[1 shl i][i] = distanceMatrix[0][i]
        }

        // Iterate over all subsets of cities of increasing sizes
        for (mask in 1 until (1 shl n)) {
            for (u in 1 until n) {
                if ((mask and (1 shl u)) == 0) continue // Skip if city `u` is not in the subset
                for (v in 1 until n) {
                    if (v == u || (mask and (1 shl v)) == 0) continue // Skip if `v` is not in the subset or is the same as `u`
                    val prevMask = mask xor (1 shl u)
                    val cost = memo[prevMask][v] + distanceMatrix[v][u]
                    if (cost < memo[mask][u]) {
                        memo[mask][u] = cost
                        parent[mask][u] = v
                    }
                }
            }
        }

        // Find the minimum cost to complete the cycle, returning to the start city
        var minCost = Double.MAX_VALUE
        var lastNode = -1
        val allVisited = (1 shl n) - 1
        for (i in 1 until n) {
            val cost = memo[allVisited xor 1][i] + distanceMatrix[i][0]
            if (cost < minCost) {
                minCost = cost
                lastNode = i
            }
        }

        // Reconstruct the path
        val path = mutableListOf<Int>()
        var currentMask = allVisited xor 1
        var currentNode = lastNode
        while (currentNode != -1) {
            path.add(currentNode)
            val nextNode = parent[currentMask][currentNode]
            currentMask = currentMask xor (1 shl currentNode)
            currentNode = nextNode
        }
        path.add(0) // Return to the starting city
        path.reverse()

        return Pair(minCost, path)
    }
}
