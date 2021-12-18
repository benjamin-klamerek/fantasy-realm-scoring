package com.klamerek.fantasyrealms.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.databinding.ActivityHandSelectionBinding
import com.klamerek.fantasyrealms.databinding.HandListItemBinding
import com.klamerek.fantasyrealms.game.*
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.Preferences
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class HandSelectionActivity : CustomActivity() {

    private lateinit var adapter: HandSelectionAdapter
    private lateinit var withGame: WithGame
    private lateinit var binding: ActivityHandSelectionBinding

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        binding = ActivityHandSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        withGame = findOrCreateGameSession()

        binding.addCardsButton.setOnClickListener {
            val handSelectionIntent = Intent(this, CardsSelectionActivity::class.java)
            val request = CardsSelectionExchange()
            request.cardsSelected.addAll(withGame.game().cards().map { card -> card.definition.id })
            handSelectionIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, request)
            startActivityForResult(handSelectionIntent, Constants.SELECT_CARDS)
        }
        binding.scanButton.setOnClickListener {
            val handSelectionIntent = Intent(this, ScanActivity::class.java)
            startActivityForResult(handSelectionIntent, Constants.SELECT_CARDS)
        }
        binding.clearButton.setOnClickListener {
            EventBus.getDefault().post(AllCardsDeletionEvent())
        }

        val linearLayoutManager = LinearLayoutManager(this)
        binding.handView.addItemDecoration(
            DividerItemDecoration(
                binding.handView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.handView.layoutManager = linearLayoutManager
        adapter = HandSelectionAdapter(
            withGame,
            Preferences.getDisplayCardNumber(binding.handView.context)
        )
        binding.handView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(
            binding.handView.context, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) { position: Int -> EventBus.getDefault().post(CardDeletionEvent(position)) })
        itemTouchHelper.attachToRecyclerView(binding.handView)

        withGame.game().calculate()
        refreshGameSessionLabels()
        adapter.notifyDataSetChanged()
    }

    private fun findOrCreateGameSession(): WithGame {
        val index = intent.getIntExtra(Constants.GAME_SESSION_ID, -1)
        return when {
            index == -1 -> {
                DiscardArea.instance
            }
            Player.all.isEmpty() -> {
                Player.all.add(Player(Player.generateNextPlayerName(), Game()))
                Player.all[0]
            }
            Player.all.size <= index -> {
                Player.all[Player.all.size - 1]
            }
            else -> {
                Player.all[index]
            }
        }
    }

    private fun refreshGameSessionLabels() {
        if (withGame.displayScore()) {
            binding.playerNameLabel.text =
                getString(R.string.player_name_with_score, withGame.name(), withGame.game().score())
            binding.handSizeLabel.text = getString(
                R.string.hand_size,
                withGame.game().actualHandSize(),
                withGame.game().handSizeExpected(baseContext)
            )
        } else {
            binding.playerNameLabel.text = withGame.name()
            binding.handSizeLabel.text = ""
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val allCardsById = CardDefinitions.getAllById()
        if (resultCode == Constants.RESULT_OK && requestCode == Constants.SELECT_CARDS) {
            val answer =
                data?.getSerializableExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID) as? CardsSelectionExchange
            withGame.game()
                .update(answer?.cardsSelected?.mapNotNull { index -> allCardsById[index] }
                    .orEmpty())
        } else if (resultCode == Constants.RESULT_OK && requestCode == Constants.SELECT_RULE_EFFECT) {
            val answer =
                data?.getSerializableExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID) as? CardsSelectionExchange
            val cardDefinition = allCardsById[answer?.cardInitiator]
            val cardSelected = allCardsById[answer?.cardsSelected?.firstOrNull()]
            val suitSelected =
                answer?.suitsSelected?.firstOrNull()?.let { name -> Suit.valueOf(name) }
            withGame.game().applySelection(cardDefinition, cardSelected, suitSelected)

        }
        withGame.game().calculate()

        refreshGameSessionLabels()
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    fun removeAllCards(event: AllCardsDeletionEvent) {
        runOnUiThread {
            withGame.game().clear()
            withGame.game().calculate()

            adapter.notifyDataSetChanged()
            refreshGameSessionLabels()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    fun removeCard(event: CardDeletionEvent) {
        runOnUiThread {
            withGame.game().remove(withGame.game().cards().elementAt(event.index).definition)
            withGame.game().calculate()

            adapter.notifyDataSetChanged()
            refreshGameSessionLabels()
        }
    }

    @Subscribe
    fun requestCardEffectSelection(event: RequestCardEffectSelectionEvent) {
        val cardDefinition = CardDefinitions.getAllById()[event.cardDefinitionId]
        val handSelectionIntent = Intent(this, CardsSelectionActivity::class.java)
        val request = CardsSelectionExchange()
        request.cardInitiator = cardDefinition?.id
        request.label = cardDefinition?.rule()
        request.selectionMode = withGame.game().ruleEffectSelectionMode(cardDefinition)
        request.cardsSelected.addAll(
            withGame.game().ruleEffectCardSelectionAbout(cardDefinition).map { it.id })
        request.suitsSelected.addAll(
            withGame.game().ruleEffectSuitSelectionAbout(cardDefinition).map { it.name })
        request.cardsScope.addAll(
            withGame.game().ruleEffectCandidateAbout(cardDefinition).map { it.id })
        handSelectionIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, request)
        startActivityForResult(handSelectionIntent, Constants.SELECT_RULE_EFFECT)
    }
}

class HandSelectionAdapter(private val withGame: WithGame, private val displayCardNumber: Boolean) :
    RecyclerView.Adapter<HandSelectionAdapter.HandHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandHolder {
        val itemBinding =
            HandListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HandHolder(itemBinding, withGame, displayCardNumber)
    }

    override fun getItemCount(): Int = withGame.game().cards().size

    override fun onBindViewHolder(holder: HandHolder, position: Int) {
        holder.bindCard(withGame.game().cards().elementAt(position))
    }

    class HandHolder(
        v: HandListItemBinding,
        private val withGame: WithGame,
        private val displayCardNumber: Boolean
    ) : RecyclerView.ViewHolder(v.root) {

        private var view: HandListItemBinding = v

        fun bindCard(card: Card) {
            updateMainPart(card)
            updateDetailPart(card)
        }

        @SuppressLint("SetTextI18n")
        private fun updateMainPart(card: Card) {
            view.cardNameLabel.text =
                if (displayCardNumber) card.definition.nameWithId()
                else card.definition.name()
            view.cardNameLabel.setChipBackgroundColorResource(card.suit().color)
            view.cardNameLabel.paintFlags =
                if (card.blanked && withGame.displayScore()) Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG else 0
            view.scoreLabel.text =
                if (withGame.displayScore()) withGame.game().score(card.definition).toString()
                else ""
            view.effectButton.visibility =
                if (withGame.game().hasManualEffect(card.definition) &&
                    withGame.displayScore()
                ) View.VISIBLE else View.GONE
            view.effectButton.setOnClickListener {
                EventBus.getDefault().post(RequestCardEffectSelectionEvent(card.definition.id))
            }
            view.cardDetailButton.visibility =
                if (withGame.displayScore()) View.VISIBLE else View.GONE
            view.cardDetailButton.setOnClickListener {
                view.detailLinearLayout.visibility =
                    if (view.detailLinearLayout.visibility == View.GONE) View.VISIBLE else View.GONE
                view.cardDetailButton.setImageResource(
                    if (view.detailLinearLayout.visibility == View.GONE)
                        R.drawable.ic_baseline_keyboard_arrow_down_36 else
                        R.drawable.ic_baseline_keyboard_arrow_up_36
                )
            }
        }

        @SuppressLint("SetTextI18n")
        private fun updateDetailPart(card: Card) {
            view.detailLinearLayout.visibility = View.GONE
            view.baseValueLabel.text = card.value().toString()

            val bonus = withGame.game().bonusScore(card.definition)
            view.bonusValueLabel.text = "+$bonus"
            view.detailBonusConstraintLayout.visibility = if (bonus > 0) View.VISIBLE else View.GONE

            val penalty = withGame.game().penaltyScore(card.definition)
            view.penaltyValueLabel.text = "$penalty"
            view.detailPenaltyConstraintLayout.visibility =
                if (penalty < 0) View.VISIBLE else View.GONE
        }

    }

}

class CardDeletionEvent(val index: Int)

class RequestCardEffectSelectionEvent(val cardDefinitionId: Int)

class AllCardsDeletionEvent
