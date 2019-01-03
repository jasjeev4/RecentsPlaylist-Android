package science.logarithmic.recentsplaylist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.android.synthetic.main.activity_scrolling.*


class ScrollingActivity : CAppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    val CLIENT_ID = "bac34290c1f0480f9a21b4aab5e2c544"

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            //val intent = Intent(this, FullscreenActivity::class.java).apply {
            //putExtra("message", "Thanks for testing!\nRestart the app to try it again another time.")
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        viewManager = LinearLayoutManager(this)

        setSupportActionBar(toolbar)
        val message = intent.getStringExtra("result")
        Log.e("Server response:", message)
        handleResponse(message)

    }

    private fun playPlaylist(playlistId: String) {
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(getRedirectUri().toString())
                .showAuthView(true)
                .build()

        SpotifyAppRemote.CONNECTOR.connect(
                application,
                connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote
                        val msar = mSpotifyAppRemote
                        msar?.playerApi?.play("spotify:user:spotify:playlist:" + playlistId)
                    }

                    override fun onFailure(error: Throwable) {
                        Log.e("Connection failed: ", error.toString())
                    }
                })
    }

    private fun handleResponse(data: String) {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        val modelAdapter = moshi.adapter(ResultModel::class.java)
        val json = modelAdapter.fromJson(data)
        //check for update
        if((json?.update?.needed != null) &&(json.update.needed<1)) {
            updateMessage(json.update.message!!)
        }
        else {
            displayTracks(json)
            updateMessage("Playlist saved to Spotify!")
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Playing Recents Playlist!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            playPlaylist(json?.result?.playlist?.playlist_id!!)
        }
    }

    private fun displayTracks(json: ResultModel?) {
        val tracks = json?.result?.playlist?.tracks
        val len = tracks!!.size
        var trackNames: Array<String> = Array(len) { "n = $it" }
        var artistNames: Array<String> = Array(len) { "n = $it" }
        var albumNames: Array<String> = Array(len) { "n = $it" }
        var i = 0
        for(track in tracks) {
            //Unsafe :(
            trackNames[i] = track.name!!
            artistNames[i] = track.artists!!
            albumNames[i] = track.album!!
            i++
        }

        viewAdapter = MyAdapter(trackNames, artistNames, albumNames)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    private fun updateMessage(message: CharSequence) {
        Snackbar.make(scrolling_activity_view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    private fun getRedirectUri(): Uri {
        return Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(getString(R.string.com_spotify_sdk_redirect_host))
                .build()
    }
}
