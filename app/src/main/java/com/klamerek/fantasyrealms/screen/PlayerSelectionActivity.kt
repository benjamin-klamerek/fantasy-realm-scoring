package com.klamerek.fantasyrealms.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.databinding.ActivityPlayerSelectionBinding
import com.klamerek.fantasyrealms.databinding.PlayerListItemBinding
import com.klamerek.fantasyrealms.game.Game
import com.klamerek.fantasyrealms.game.Player
import com.klamerek.fantasyrealms.util.Constants
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Activity to add and remove players
 *
 */
class PlayerSelectionActivity : CustomActivity() {

    @Suppress("MagicNumber")
    private val delayBeforeShowingKeyboard = 200L

    private lateinit var adapter: PlayerSelectionAdapter
    private lateinit var binding: ActivityPlayerSelectionBinding

    @SuppressLint("NotifyDataSetChanged")
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
        binding = ActivityPlayerSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        installDialog()

        val linearLayoutManager = LinearLayoutManager(this)
        binding.playersView.addItemDecoration(DividerItemDecoration(binding.playersView.context, DividerItemDecoration.VERTICAL))
        binding.playersView.layoutManager = linearLayoutManager
        adapter = PlayerSelectionAdapter(Player.all)
        binding.playersView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(
            binding.playersView.context, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) { position: Int -> EventBus.getDefault().post(PlayerDeletionEvent(position)) })
        itemTouchHelper.attachToRecyclerView(binding.playersView)
    }

    @SuppressLint("InflateParams")
    private fun installDialog() {
        val dialogView: View = this.layoutInflater.inflate(R.layout.dialog_new_player, null)
        val field: TextInputEditText? = dialogView.findViewWithTag("playerNameEditText")
        val dialog = initDialog(dialogView, field)
        binding.addPlayerButton.setOnClickListener {
            field?.text?.clear()
            field?.setText(Player.generateNextPlayerName())
            dialog.show()
            field?.requestFocus()
            field?.selectAll()
            field?.postDelayed({
                val keyboard: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.showSoftInput(field, 0)
            }, delayBeforeShowingKeyboard)
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
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = ! (field?.text?.isBlank()?:true)
        }
        field?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = ! s.isNullOrBlank()
            }

        })
        return alertDialog
    }

    @Subscribe
    fun addPlayer(event: PlayerCreationEvent) {
        Player.all.add(Player(event.name, Game()))
        runOnUiThread {
            adapter.notifyItemInserted(Player.all.size - 1)
        }
    }

    @Subscribe
    fun removePlayer(event: PlayerDeletionEvent) {
        Player.all.removeAt(event.index)
        runOnUiThread {
            adapter.notifyItemRemoved(event.index)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UnusedPrivateMember")
    @Subscribe
    fun removeAllPlayers(event: AllPlayersDeletionEvent) {
        Player.all.clear()
        runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }

    @Subscribe
    fun editPlayer(event: PlayerEditEvent) {
        val handSelectionIntent = Intent(this, HandSelectionActivity::class.java)
        handSelectionIntent.putExtra(Constants.PLAYER_SESSION_ID, Player.all.indexOf(event.player))
        startActivity(handSelectionIntent)
    }

}

class PlayerSelectionAdapter(private val players: Collection<Player>) : RecyclerView.Adapter<PlayerSelectionAdapter.PlayerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        val itemBinding = PlayerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerHolder(itemBinding)
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        holder.bindPlayer(players.elementAt(position))
    }

    class PlayerHolder(v: PlayerListItemBinding) : RecyclerView.ViewHolder(v.root) {

        private var view: PlayerListItemBinding = v

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

class AllPlayersDeletionEvent

class PlayerEditEvent(val player: Player)
