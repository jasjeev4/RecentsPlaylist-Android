package science.logarithmic.recentsplaylist

data class ResultModel (
        val status: Int?,
        val update: Update?,
        val result: Result?
)

data class Update (
        val needed: Int?,
        val message: String?
)

data class Result (
        val playlist: Playlist?
)

data class Playlist (
        val user_id: String?,
        val playlist_id: String?,
        val tracks: List<Tracks>?
        )

data class Tracks (
        val track_id: String?,
        val added_date: String?,
        val name: String?,
        val artists: String?,
        val album: String?
)
