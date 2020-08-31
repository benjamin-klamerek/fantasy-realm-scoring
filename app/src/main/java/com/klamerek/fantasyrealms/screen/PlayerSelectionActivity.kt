package com.klamerek.fantasyrealms.screen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.Game
import com.klamerek.fantasyrealms.game.Player
import com.klamerek.fantasyrealms.game.Players
import inflate
import kotlinx.android.synthetic.main.activity_player_selection.*
import kotlinx.android.synthetic.main.player_list_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class PlayerSelectionActivity : AppCompatActivity() {

    private lateinit var adapter: PlayerSelectionAdapter

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        setContentView(R.layout.activity_player_selection)

        installDialog()

        val linearLayoutManager = LinearLayoutManager(this)
        playersView.addItemDecoration(DividerItemDecoration(playersView.context, DividerItemDecoration.VERTICAL))
        playersView.layoutManager = linearLayoutManager
        adapter = PlayerSelectionAdapter(Players.instance)
        playersView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(
            playersView.context, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) { position: Int -> EventBus.getDefault().post(PlayerDeletionEvent(position)) })
        itemTouchHelper.attachToRecyclerView(playersView)
    }

    private fun installDialog() {
        val dialogView: View = this.layoutInflater.inflate(R.layout.dialog_new_player, null)
        val field: TextInputEditText? = dialogView.findViewWithTag("playerNameEditText")
        val dialog = initDialog(dialogView, field)
        addPlayerButton.setOnClickListener {
            field?.text?.clear()
            dialog.show()
        }
    }

    private fun initDialog(dialogView: View, field: TextInputEditText?): AlertDialog {
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(R.string.ok_button) { _, _ ->
                EventBus.getDefault().post(
                    PlayerCreationEvent(field?.text?.toString() ?: getString(R.string.new_player_default_value))
                )
            }
            .setNegativeButton(R.string.cancel_button) { _, _ -> }
            .create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false
        }
        field?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !s.isNullOrBlank()
            }

        })
        return alertDialog
    }

    @Subscribe
    fun addPlayer(event: PlayerCreationEvent) {
        Players.instance.add(Player(event.name, Game()))
        runOnUiThread {
            adapter.notifyItemInserted(Players.instance.size - 1)
        }
    }

    @Subscribe
    fun removePlayer(event: PlayerDeletionEvent) {
        Players.instance.removeAt(event.index)
        runOnUiThread {
            adapter.notifyItemRemoved(event.index)
        }
    }

    @Subscribe
    fun editPlayer(event: PlayerEditEvent) {
        val handSelectionIntent = Intent(this, HandSelectionActivity::class.java)
        handSelectionIntent.putExtra(Constants.PLAYER_SESSION_ID, Players.instance.indexOf(event.player))
        startActivity(handSelectionIntent)
    }

}

class PlayerSelectionAdapter(private val players: Collection<Player>) : RecyclerView.Adapter<PlayerSelectionAdapter.PlayerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        val inflatedView = parent.inflate(R.layout.player_list_item, false)
        return PlayerHolder(inflatedView)
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        holder.bindPlayer(players.elementAt(position))
    }

    class PlayerHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bindPlayer(player: Player) {
            view.playerNameField.text = player.name
            view.scoreLabel.text = player.game.score().toString()
            view.editButton.setOnClickListener {
                EventBus.getDefault().post(PlayerEditEvent(player))
            }
        }

    }

}

class PlayerCreationEvent(val name: String)

class PlayerDeletionEvent(val index: Int)

class PlayerEditEvent(val player: Player)
