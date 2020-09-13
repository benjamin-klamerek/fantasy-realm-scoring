package com.klamerek.fantasyrealms.screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.*
import inflate
import kotlinx.android.synthetic.main.activity_hand_selection.*
import kotlinx.android.synthetic.main.hand_list_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class HandSelectionActivity : AppCompatActivity() {

    private lateinit var adapter: HandSelectionAdapter
    private lateinit var player: Player

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_hand_selection)

        player = Players.instance[intent.getIntExtra(Constants.PLAYER_SESSION_ID, 0)]

        refreshPlayerLabels()

        addCardsButton.setOnClickListener {
            val handSelectionIntent = Intent(this, CardsSelectionActivity::class.java)
            val request = CardsSelectionExchange()
            request.cardsSelected.addAll(player.game.cards().map { card -> card.definition.id })
            handSelectionIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, request)
            startActivityForResult(handSelectionIntent, Constants.SELECT_CARDS)
        }
        scanButton.setOnClickListener {
            val handSelectionIntent = Intent(this, ScanActivity::class.java)
            startActivityForResult(handSelectionIntent, Constants.SELECT_CARDS)
        }

        val linearLayoutManager = LinearLayoutManager(this)
        handView.addItemDecoration(DividerItemDecoration(handView.context, DividerItemDecoration.VERTICAL))
        handView.layoutManager = linearLayoutManager
        adapter = HandSelectionAdapter(player.game)
        handView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(
            handView.context, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) { position: Int -> EventBus.getDefault().post(CardDeletionEvent(position)) })
        itemTouchHelper.attachToRecyclerView(handView)
    }

    private fun refreshPlayerLabels() {
        playerNameLabel.text = player.name + " - Score : " + player.game.score()
        handSizeLabel.text = player.game.actualHandSize().toString() + "/" + player.game.handSizeExpected()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.RESULT_OK && requestCode == Constants.SELECT_CARDS) {
            val answer = data?.getSerializableExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID) as? CardsSelectionExchange
            player.game.update(answer?.cardsSelected?.mapNotNull { index -> cardsById[index] }.orEmpty())
        } else if (resultCode == Constants.RESULT_OK && requestCode == Constants.SELECT_RULE_EFFECT) {
            val answer = data?.getSerializableExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID) as? CardsSelectionExchange
            val cardDefinition = cardsById[answer?.cardInitiator]
            val cardSelected = cardsById[answer?.cardsSelected?.firstOrNull()]
            val suitSelected = answer?.suitsSelected?.firstOrNull()?.let { name -> Suit.valueOf(name) }
            when (cardDefinition) {
                bookOfChanges -> player.game.bookOfChangeSelection = Pair(cardSelected, suitSelected)
                island -> player.game.islandSelection = cardSelected
                shapeshifter -> player.game.shapeShifterSelection = cardSelected
                mirage -> player.game.mirageSelection = cardSelected
                doppelganger -> player.game.doppelgangerSelection = cardSelected
            }
        }
        player.game.calculate()
        runOnUiThread {
            refreshPlayerLabels()
            adapter.notifyDataSetChanged()
        }
    }

    @Subscribe
    fun removeAllCards(event: AllCardsDeletionEvent) {
        player.game.clear()
        runOnUiThread {
            adapter.notifyDataSetChanged()
            refreshPlayerLabels()
        }
    }

    @Subscribe
    fun removeCard(event: CardDeletionEvent) {
        player.game.remove(player.game.cards().elementAt(event.index).definition)
        player.game.calculate()
        runOnUiThread {
            adapter.notifyDataSetChanged()
            refreshPlayerLabels()
        }
    }

    @Subscribe
    fun requestCardEffectSelection(event: RequestCardEffectSelectionEvent) {
        val cardDefinition = cardsById[event.cardDefinitionId]
        val handSelectionIntent = Intent(this, CardsSelectionActivity::class.java)
        val request = CardsSelectionExchange()
        request.cardInitiator = cardDefinition?.id
        request.label = cardDefinition?.rule()
        request.selectionMode = player.game.ruleEffectSelectionMode(cardDefinition)
        request.cardsSelected.addAll(player.game.ruleEffectCardSelectionAbout(cardDefinition).map { definition -> definition.id })
        request.suitsSelected.addAll(player.game.ruleEffectSuitSelectionAbout(cardDefinition).map { suit -> suit.name })
        request.cardsScope.addAll(player.game.ruleEffectCandidateAbout(cardDefinition).map { definition -> definition.id })
        handSelectionIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, request)
        startActivityForResult(handSelectionIntent, Constants.SELECT_RULE_EFFECT)
    }
}

class HandSelectionAdapter(private val game: Game) : RecyclerView.Adapter<HandSelectionAdapter.HandHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandHolder {
        val inflatedView = parent.inflate(R.layout.hand_list_item, false)
        return HandHolder(inflatedView, game)
    }

    override fun getItemCount(): Int = game.actualHandSize()

    override fun onBindViewHolder(holder: HandHolder, position: Int) {
        holder.bindCard(game.cards().elementAt(position))
    }

    class HandHolder(v: View, private val game: Game) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bindCard(card: Card) {
            updateMainPart(card)
            updateDetailPart(card)
        }

        private fun updateMainPart(card: Card) {
            view.cardNameLabel.text = card.definition.name()
            view.cardNameLabel.setChipBackgroundColorResource(card.suit().color)
            view.scoreLabel.text = game.score(card.definition).toString()
            view.effectButton.visibility = if (game.hasManualEffect(card.definition)) View.VISIBLE else View.GONE
            view.effectButton.setOnClickListener {
                EventBus.getDefault().post(RequestCardEffectSelectionEvent(card.definition.id))
            }
            view.cardDetailButton.setOnClickListener {
                view.detailLinearLayout.visibility = if (view.detailLinearLayout.visibility == View.GONE) View.VISIBLE else View.GONE
                view.cardDetailButton.setImageResource(
                    if (view.detailLinearLayout.visibility == View.GONE) R.drawable.ic_baseline_keyboard_arrow_down_36 else R.drawable.ic_baseline_keyboard_arrow_up_36
                )
            }
        }

        private fun updateDetailPart(card: Card) {
            view.detailLinearLayout.visibility = View.GONE
            view.baseValueLabel.text = card.value().toString()

            val bonus = game.bonusScore(card.definition)
            view.bonusValueLabel.text = "+$bonus"
            view.detailBonusConstraintLayout.visibility = if (bonus > 0) View.VISIBLE else View.GONE

            val penalty = game.penaltyScore(card.definition)
            view.penaltyValueLabel.text = "$penalty"
            view.detailPenaltyConstraintLayout.visibility = if (penalty < 0) View.VISIBLE else View.GONE
        }

    }

}

class CardDeletionEvent(val index: Int)

class RequestCardEffectSelectionEvent(val cardDefinitionId: Int)

class AllCardsDeletionEvent