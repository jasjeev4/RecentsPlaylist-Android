package science.logarithmic.recentsplaylist

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_item.view.*

class MyAdapter(private val trackNames: Array<String>, private val artistNames: Array<String>, private val albumNames: Array<String>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.track_name.text = trackNames[position]
        holder.cardView.track_artist.text = artistNames[position]
        holder.cardView.track_album.text = albumNames[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = trackNames.size
}